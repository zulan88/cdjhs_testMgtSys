package net.wanji.business.trajectory;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.RequiredArgsConstructor;
import net.wanji.business.common.Constants;
import net.wanji.business.common.Constants.TestingStatusEnum;
import net.wanji.business.domain.RealWebsocketMessage;
import net.wanji.business.domain.dto.ToLocalDto;
import net.wanji.business.entity.TjCaseRealRecord;
import net.wanji.business.entity.TjTaskCaseRecord;
import net.wanji.business.exercise.LuanshengDataSender;
import net.wanji.business.exercise.dto.evaluation.StartPoint;
import net.wanji.business.exercise.dto.jidaevaluation.trajectory.RealTimeParticipant;
import net.wanji.business.exercise.dto.jidaevaluation.trajectory.RealTimeTrajectory;
import net.wanji.business.exercise.dto.luansheng.StatCache;
import net.wanji.business.exercise.utils.ToBuildOpenXTransUtil;
import net.wanji.business.listener.KafkaCollector;
import net.wanji.business.mapper.TjCaseRealRecordMapper;
import net.wanji.business.mapper.TjTaskCaseRecordMapper;
import net.wanji.business.service.KafkaProducer;
import net.wanji.business.service.record.DataFileService;
import net.wanji.business.socket.WebSocketManage;
import net.wanji.business.util.RedisLock;
import net.wanji.common.common.ClientSimulationTrajectoryDto;
import net.wanji.common.common.TrajectoryValueDto;
import net.wanji.common.constant.CacheConstants;
import net.wanji.common.core.redis.RedisCache;
import net.wanji.common.utils.DateUtils;
import net.wanji.common.utils.RedisKeyUtils;
import net.wanji.common.utils.StringUtils;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.awt.geom.Point2D;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @Auther: guanyuduo
 * @Date: 2023/7/5 14:00
 * @Descriptoin:
 */
@Component
@RequiredArgsConstructor
public class KafkaTrajectoryConsumer {
    private static final Logger log = LoggerFactory.getLogger("kafka");

    private static String proj = "+proj=tmerc +lon_0=108.90577060170472 +lat_0=34.37650478465651 +ellps=WGS84";
    /**
     * fileId:ToLocalDto
     */
    private static final Set<ToLocalDto> toLocalSet = new HashSet<>();
    @Resource
    private TjTaskCaseRecordMapper taskCaseRecordMapper;
    @Resource
    private TjCaseRealRecordMapper caseRealRecordMapper;

    private final KafkaCollector kafkaCollector;
    private final RedisLock redisLock;
    private final RedisCache redisCache;
    private final DataFileService dataFileService;
    private final KafkaProducer kafkaProducer;
    private final LuanshengDataSender luanshengDataSender;

    @KafkaListener(id = "singleTrajectory",
            topics = { "${trajectory.fusion}" },
            groupId = "#{T(java.lang.String).valueOf(new java.util.Random().nextInt(1000))}")
    public void listen(ConsumerRecord<String, String> record) {
        String value = record.value();
        JSONObject jsonObject = JSONObject.parseObject(value);
        Integer taskId = jsonObject.getInteger("taskId");
        Integer caseId = jsonObject.getInteger("caseId");

        ToLocalDto toLocalDto = queryTolocalDto(taskId, caseId);
        if(Objects.nonNull(toLocalDto)){
            ch.qos.logback.classic.Logger logger = toLocalDto.getLogger();
            logger.info(value);
            JSONArray participantTrajectories = jsonObject.getJSONArray("participantTrajectories");
            //数据融合写入文件
            List<ClientSimulationTrajectoryDto> data = participantTrajectories.toJavaList(ClientSimulationTrajectoryDto.class);
            //过滤掉没有主车的数据
            boolean avNotExisted = data.stream()
                    .noneMatch(item -> item.getRole().equals(Constants.PartRole.AV));
            if(avNotExisted){
                log.info("任务-{}的融合数据没有主车:{}", taskId, jsonObject.toString());
                return;
            }

            toLocalDto.getToLocalThread()
                    .write(participantTrajectories.toJSONString());
            //实时轨迹发送websocket
            int size = toLocalDto.getCount().incrementAndGet();
            String username = toLocalDto.getUsername();
            String key = taskId > 0 ?
                    Constants.ChannelBuilder.buildOnlineTaskPlaybackChannel(username, taskId) :
                    Constants.ChannelBuilder.buildTestingDataChannel(username, caseId);
            String duration = DateUtils.secondsToDuration((int) Math.floor((size) / 10.0));
            //获取主车
            String mainChannel = toLocalDto.getMainChannel();
            ClientSimulationTrajectoryDto mainCar = data.stream()
                    .filter(n -> mainChannel.equals(n.getSource()))
                    .collect(Collectors.toList()).get(0);
            //主车位置
            Double latitude = mainCar.getValue().get(0).getLatitude();
            Double longitude = mainCar.getValue().get(0).getLongitude();
            Point2D.Double position = new Point2D.Double(longitude, latitude);
            //计算待触发场景
            toLocalDto.calculate(position);
            List<StartPoint> sceneStartPoints = toLocalDto.getStartPoints();
            //比赛任务推送孪生
            int index = toLocalDto.getSequence() > -1 ? toLocalDto.getSequence() : 0;
            boolean qualified = toLocalDto.isCompetition();
            if(qualified){
                String sceneName = sceneStartPoints.get(index).getName();
                luanshengDataSender.send(data, taskId, sceneName);
                //统计图标数据缓存
                StatCache.update(taskId, mainCar.getValue().get(0), toLocalDto);
            }
            //推送webgl
            RealWebsocketMessage msg = new RealWebsocketMessage(
                    Constants.RedisMessageType.TRAJECTORY, sceneStartPoints, data, duration, toLocalDto.getTriggeredScenes());
            WebSocketManage.sendInfo(key, JSONObject.toJSONString(msg));
            //向济达发送实时轨迹
            if(StringUtils.isNotEmpty(toLocalDto.getKafkaTopic())){
                Integer sceneId = sceneStartPoints.get(index).getSceneId();
                sendRealTimeTrajecotory(toLocalDto, sceneId, data);
            }
        }
    }

    private ToLocalDto queryTolocalDto(Integer taskId, Integer caseId) {
        for (ToLocalDto toLocalDto : toLocalSet) {
            if (toLocalDto.getTaskId().equals(taskId) && toLocalDto.getCaseId()
                    .equals(caseId)) {
                return toLocalDto;
            }
        }
        return null;
    }

    public boolean subscribe(ToLocalDto toLocalDto) {
        toLocalDto.setToLocalThread(
                dataFileService.createToLocalThread(toLocalDto));
        toLocalSet.add(toLocalDto);
        return true;
    }

    public boolean unSubscribe(ToLocalDto toLocalDto) {
        try {
            Optional<ToLocalDto> localOp = toLocalSet.stream()
                    .filter(e -> e.equals(toLocalDto)).findFirst();
            if (localOp.isPresent()) {
                ToLocalDto oldToLocal = localOp.get();
                dataFileService.writeStop(oldToLocal);
                return toLocalSet.remove(oldToLocal);
            }
            return false;
        } catch (Exception e) {
            if (log.isErrorEnabled()) {
                log.error("unSubscribe [{}] error!", toLocalDto, e);
            }
            return false;
        }
    }

    private void sendRealTimeTrajecotory(ToLocalDto toLocalDto, Integer sceneId, List<ClientSimulationTrajectoryDto> participants) {
        String kafkaTopic = toLocalDto.getKafkaTopic();
        //筛选出主车
        if(StringUtils.isEmpty(toLocalDto.getMainVehicleId())){
            Optional<ClientSimulationTrajectoryDto> mainCar = participants.stream()
                    .filter(participant -> participant.getRole().equals(Constants.PartRole.AV))
                    .findFirst();
            if(mainCar.isPresent()){
                String mainVehicleId = mainCar.get().getValue().get(0).getId();
                toLocalDto.setMainVehicleId(mainVehicleId);
            }
        }
        //数据组装
        RealTimeTrajectory realTimeTrajectory = new RealTimeTrajectory();
        realTimeTrajectory.setEventId(String.valueOf(toLocalDto.getTaskId()));
        realTimeTrajectory.setSimuTime(System.currentTimeMillis());
        List<RealTimeParticipant> data = new ArrayList<>();
        for(ClientSimulationTrajectoryDto participant: participants){
            String role = participant.getRole();
            boolean isMain = role.equals(Constants.PartRole.AV);
            List<TrajectoryValueDto> value = participant.getValue();
            List<RealTimeParticipant> realTimeParticipants = value.stream()
                    .map(item -> {
                        RealTimeParticipant realTimeParticipant = new RealTimeParticipant();
                        realTimeParticipant.setId(Integer.parseInt(item.getId()));
                        //坐标转换
                        JSONObject totrans = ToBuildOpenXTransUtil.totrans(item.getLongitude(), item.getLatitude(), proj);
                        realTimeParticipant.setX(totrans.getDoubleValue("x"));
                        realTimeParticipant.setY(totrans.getDoubleValue("y"));
                        realTimeParticipant.setLength(Double.parseDouble(String.format("%.2f", item.getLength() / 100.0)));
                        realTimeParticipant.setWidth(Double.parseDouble(String.format("%.2f", item.getWidth() / 100.0)));
                        realTimeParticipant.setHeight(Double.parseDouble(String.format("%.2f", item.getHeight() / 100.0)));
                        realTimeParticipant.setSpeed(item.getSpeed().doubleValue());
                        realTimeParticipant.setAngle(item.getCourseAngle());
                        realTimeParticipant.setAcce(item.getLonAcc());
                        realTimeParticipant.setIsMain(isMain);

                        //主车轨迹中增加场景id
                        if(isMain){
                            realTimeParticipant.setRegionalId(sceneId);
                            realTimeParticipant.setIsSecurityInvolved(item.getAutoStatus() == 0 ? 1 : 0);
                        }

                        return realTimeParticipant;
                    }).collect(Collectors.toList());

            data.addAll(realTimeParticipants);
        }
        realTimeTrajectory.setData(data);
        String json = JSONObject.toJSONString(realTimeTrajectory);
        kafkaProducer.sendMessage(kafkaTopic, json);

    }
}
