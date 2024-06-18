package net.wanji.business.trajectory;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.netty.util.concurrent.DefaultThreadFactory;
import net.wanji.business.common.Constants.PartRole;
import net.wanji.business.common.Constants.RedisMessageType;
import net.wanji.business.component.CountDown;
import net.wanji.business.component.PathwayPoints;
import net.wanji.business.domain.RealWebsocketMessage;
import net.wanji.business.domain.bo.CaseTrajectoryDetailBo;
import net.wanji.business.domain.bo.ParticipantTrajectoryBo;
import net.wanji.business.domain.bo.TaskCaseConfigBo;
import net.wanji.business.domain.bo.TaskCaseInfoBo;
import net.wanji.business.domain.bo.TrajectoryDetailBo;
import net.wanji.business.domain.dto.CountDownDto;
import net.wanji.business.domain.vo.TaskDcVo;
import net.wanji.business.entity.TjCase;
import net.wanji.business.entity.TjTaskCase;
import net.wanji.business.entity.TjTaskCaseRecord;
import net.wanji.business.entity.TjTaskDc;
import net.wanji.business.mapper.TjCaseMapper;
import net.wanji.business.mapper.TjTaskCaseMapper;
import net.wanji.business.mapper.TjTaskCaseRecordMapper;
import net.wanji.business.mapper.TjTaskDcMapper;
import net.wanji.business.service.RouteService;
import net.wanji.business.socket.WebSocketManage;
import net.wanji.common.common.RealTestTrajectoryDto;
import net.wanji.common.common.SimulationMessage;
import net.wanji.common.common.SimulationTrajectoryDto;
import net.wanji.common.common.TrajectoryValueDto;
import net.wanji.common.utils.DataUtils;
import net.wanji.common.utils.DateUtils;
import net.wanji.common.utils.SecurityUtils;
import net.wanji.common.utils.StringUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import javax.annotation.PostConstruct;
import java.awt.geom.Point2D;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Optional;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @Auther: guanyuduo
 * @Date: 2023/8/9 9:28
 * @Descriptoin:
 */
@Component
public class TaskRedisTrajectoryConsumer {

    private static final Logger log = LoggerFactory.getLogger("business");

    private final ConcurrentHashMap<String, List<ChannelListener<SimulationTrajectoryDto>>> runningChannel = new ConcurrentHashMap<>();

    private final RedisMessageListenerContainer redisMessageListenerContainer;

    private final ConcurrentHashMap<String, Integer> runningCase = new ConcurrentHashMap<>();

    public TaskRedisTrajectoryConsumer(RedisMessageListenerContainer redisMessageListenerContainer) {
        this.redisMessageListenerContainer = redisMessageListenerContainer;
    }

    @Autowired
    private RouteService routeService;

    @Autowired
    private TjCaseMapper caseMapper;

    @Autowired
    private TjTaskCaseMapper taskCaseMapper;

    @Autowired
    private TjTaskCaseRecordMapper taskCaseRecordMapper;

    @Autowired
    private TjTaskDcMapper taskDcMapper;

    @PostConstruct
    public void validChannel() {
//        ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(1,
//                new DefaultThreadFactory("TaskRedisTrajectoryConsumer-removeListeners"));
//        scheduledExecutorService.scheduleAtFixedRate(
//                this::removeListeners, 0, 60, TimeUnit.SECONDS);
    }


    /**
     * 订阅轨迹
     *
     * @param taskCaseInfoBo 任务用例信息
     * @return
     */
    public void subscribeAndSend(String key, Integer taskId, Integer taskCaseId, List<TaskCaseInfoBo> taskCaseInfos) throws IOException {
        // 添加监听器
        this.addRunningChannel(key, taskId, taskCaseId, taskCaseInfos);
    }

    public void addRunningChannel(String key, Integer taskId, Integer taskCaseId, List<TaskCaseInfoBo> taskCaseInfos) throws IOException {
        if (this.runningChannel.containsKey(key)) {
            log.info("通道 {} 已存在", key);
            return;
        }
        List<TaskCaseConfigBo> taskCaseConfigs = taskCaseInfos.stream().map(TaskCaseInfoBo::getDataConfigs)
                .filter(Objects::nonNull)
                .flatMap(List::stream)
                .collect(Collectors.collectingAndThen(Collectors.toCollection(() ->
                        new TreeSet<>(Comparator.comparing(TaskCaseConfigBo::getDeviceId))), ArrayList::new));

        // todo TjTaskCaseRecord
        MessageListener listener = createListener(key, taskCaseInfos);
        List<ChannelTopic> topics = taskCaseConfigs.stream().map(TaskCaseConfigBo::getDataChannel).map(ChannelTopic::new).collect(Collectors.toList());
        List<ChannelListener<SimulationTrajectoryDto>> listeners = new ArrayList<>();
        for (TaskCaseConfigBo taskCaseConfigBo : taskCaseConfigs) {
            ChannelListener<SimulationTrajectoryDto> channelListener =
                    new ChannelListener<>(
                            taskCaseConfigBo.getDataChannel(), SecurityUtils.getUsername(),
                            taskCaseConfigBo.getSupportRoles(), System.currentTimeMillis(), listener);
            listeners.add(channelListener);
        }
        this.runningChannel.put(key, listeners);
        redisMessageListenerContainer.addMessageListener(listener, topics);
        log.info("添加监听器 {} 成功", JSON.toJSONString(topics));
    }


    /**
     * 订阅测试用例轨迹
     *
     * @param key           任务用例key
     * @param taskCaseInfos 任务用例信息
     */
    public MessageListener createListener(String key, List<TaskCaseInfoBo> taskCaseInfos) throws IOException {

        JSONObject caseParam = new JSONObject();
        for (TaskCaseInfoBo taskCaseInfo : taskCaseInfos) {
            JSONObject paramMap = new JSONObject();
            // 任务用例信息 TaskCaseInfoBo
            paramMap.put("caseInfo", taskCaseInfo);
            // 主车轨迹
            List<RealTestTrajectoryDto> realTestTrajectoryDtos = routeService.readRealTrajectoryFromRouteFile(taskCaseInfo.getRouteFile());
            realTestTrajectoryDtos.stream().filter(RealTestTrajectoryDto::isMain).findFirst().ifPresent(t -> {
                paramMap.put("main", t.getData());
            });
            taskCaseInfo.getDataConfigs().stream().filter(item -> PartRole.AV.equals(item.getSupportRoles()))
                    .findFirst().ifPresent(t -> {
                        // av配置 TaskCaseConfigBo
                        paramMap.put(t.getDataChannel(), t);
                        // av车ID和途径点映射
                        JSONObject.parseObject(taskCaseInfo.getDetailInfo(), CaseTrajectoryDetailBo.class)
                                .getParticipantTrajectories().stream()
                                .filter(item -> t.getParticipatorId().equals(item.getId())).findFirst().ifPresent(p -> {
                                    // 主车全程、剩余时间、到达时间
                                    ArrayList<Point2D.Double> doubles = new ArrayList<>();
                                    for (TrajectoryDetailBo trajectoryDetailBo : p.getTrajectory()) {
                                        String[] pos = trajectoryDetailBo.getPosition().split(",");
                                        doubles.add(new Point2D.Double(Double.parseDouble(pos[0]), Double.parseDouble(pos[1])));
                                    }
                                    // 主车全程、剩余时间、到达时间
                                    paramMap.put("avCountDown", new CountDown(doubles));
                                    // 主车途径点提示
                                    paramMap.put("avPathwayPoints", new PathwayPoints(p.getTrajectory()));
                                });

                    });
            caseParam.put(String.valueOf(taskCaseInfo.getCaseId()), paramMap);
        }

        Integer taskId = taskCaseInfos.get(0).getTaskId();
        Map<String, Object> mainInfoMap = new HashMap<>();
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return (message, pattern) -> {
            Integer runningCase = getRunningCase(key);
//            if (runningCase == null) {
//                log.error("任务{} - 用例{}测试 - 未找到正在运行的用例", taskId, taskCaseInfos.get(0).getCaseId());
//                return;
//            }
            String methodLog = StringUtils.format("任务{} - 用例{}测试 - ", taskId, runningCase);
            try {
                String channel = new String(message.getChannel());
                SimulationMessage simulationMessage = objectMapper.readValue(message.toString(), SimulationMessage.class);
                if (!ObjectUtils.isEmpty(simulationMessage.getValue()) && simulationMessage.getValue() instanceof LinkedHashMap) {
                    LinkedHashMap value = (LinkedHashMap) simulationMessage.getValue();
                    value.remove("@type");
                }
                ChannelListener<SimulationTrajectoryDto> channelListener = this.getListener(key, channel);
                if (ObjectUtils.isEmpty(channelListener)) {
                    log.error(StringUtils.format("{}查询监听器异常：{} - {}", methodLog, key, channel));
                    return;
                }
                JSONObject paramItem = (JSONObject) caseParam.get(String.valueOf(runningCase));
                String duration = DateUtils.secondsToDuration(
                        (int) Math.floor((double) (getDataSize(key, channel)) / 10));
                switch (simulationMessage.getType()) {
                    case RedisMessageType.TRAJECTORY:
                        SimulationTrajectoryDto simulationTrajectory = objectMapper.readValue(
                                JSON.toJSONString(simulationMessage.getValue()), SimulationTrajectoryDto.class);
                        // 实际轨迹消息
                        List<TrajectoryValueDto> data = simulationTrajectory.getValue();
                        // 无论是否有轨迹都保存
                        receiveData(key, channel, simulationTrajectory);
                        if (CollectionUtils.isNotEmpty(data)) {
                            // send ws
                            RealWebsocketMessage msg = null;
                            if (!ObjectUtils.isEmpty(paramItem) && paramItem.containsKey(channel)) {
                                try {
                                    // 速度
                                    mainInfoMap.put("speed", data.get(0).getSpeed());

                                    // 途径点距离、速度
                                    PathwayPoints pathwayPoints = paramItem.getObject("avPathwayPoints", PathwayPoints.class);
                                    PathwayPoints nearestPoint = pathwayPoints.findNearestPoint(data.get(0).getLongitude(), data.get(0).getLatitude());
                                    TaskCaseConfigBo caseConfig = paramItem.getObject(channel, TaskCaseConfigBo.class);
                                    Map<String, Object> tipsMap = new HashMap<>();
                                    if (nearestPoint.hasTips()) {
                                        tipsMap.put("id", caseConfig.getParticipatorId());
                                        tipsMap.put("name", caseConfig.getParticipatorName());
                                        tipsMap.put("pointName", nearestPoint.getPointName());
                                        tipsMap.put("pointDistance", nearestPoint.getDistance());
                                        tipsMap.put("pointSpeed", nearestPoint.getPointSpeed());
                                    }
                                    mainInfoMap.put("tips", tipsMap);
                                    // 仿真车未来轨迹
                                    List<Map<String, Double>> futureList = new ArrayList<>();
//                                if (!CollectionUtils.isEmpty(mainSimuTrajectories)) {
//                                    // 仿真验证中当前主车位置
//                                    data.add(mainSimuTrajectories.remove(0));
//                                    // 仿真验证中主车剩余轨迹
//                                    futureList = mainSimuTrajectories.stream().map(item -> {
//                                        Map<String, Double> posMap = new HashMap<>();
//                                        posMap.put("longitude", item.getLongitude());
//                                        posMap.put("latitude", item.getLatitude());
//                                        return posMap;
//                                    }).collect(Collectors.toList());
//                                }
                                    mainInfoMap.put("simuFuture", futureList);
                                } catch (Exception e) {
                                    log.error("{}主车信息计算异常：{}", methodLog, e);
                                }
                                msg = new RealWebsocketMessage(RedisMessageType.TRAJECTORY, mainInfoMap, data,
                                        duration);
                            } else {
                                msg = new RealWebsocketMessage(RedisMessageType.TRAJECTORY, null, data,
                                        duration);
                            }
                            WebSocketManage.sendInfo(key.concat("_").concat(channel), JSON.toJSONString(msg));
                        }
                        break;
                    case RedisMessageType.END:
//                        JSONObject end = JSONObject.parseObject(JSON.toJSONString(simulationMessage.getValue()));
//                        JSONObject end = objectMapper.readValue(JSON.toJSONString(simulationMessage.getValue()), JSONObject.class);
//                        log.info(StringUtils.format("结束接收{}数据：{}", methodLog, JSON.toJSONString(end)));
//                        try {
//                            Optional.ofNullable(end.getEvaluationVerify()).ifPresent(originalTrajectory::setEvaluationVerify);
//                        } catch (Exception e) {
//                            originalTrajectory.setEvaluationVerify("True");
//                        }
//                        TaskCaseInfoBo caseInfo = paramItem.getObject("caseInfo", TaskCaseInfoBo.class);
//                        CaseTrajectoryDetailBo originalTrajectory = JSONObject.parseObject(caseInfo.getDetailInfo(), CaseTrajectoryDetailBo.class);
//                        TjTaskCaseRecord param = new TjTaskCaseRecord();
//                        param.setId(caseInfo.getRecordId());
//                        originalTrajectory.setDuration(duration);
//                        param.setDetailInfo(JSON.toJSONString(originalTrajectory));
//                        int endSuccess = taskCaseRecordMapper.updateById(param);
//                        log.info(StringUtils.format("修改用例场景评价:{}", endSuccess));
//                        clearRunningCase(String.valueOf(caseInfo.getCaseId()));
//                        save(key, caseInfo.getRecordId(), originalTrajectory, end.getInteger("action"));
//                        if (end.getBoolean("taskEnd")) {
//                            removeListener(key);
//                            RealWebsocketMessage endMsg = new RealWebsocketMessage(RedisMessageType.END, null, null,
//                                    duration);
//                            WebSocketManage.sendInfo(key.concat("_").concat(channel), JSON.toJSONString(endMsg));
//                        }
                        break;
                    default:
                        break;
                }
            } catch (Exception e) {
                e.printStackTrace();
                removeListener(key);
            }
        };
    }


    public void handleScore(Integer taskId, String score) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            String scoreJsonString = score.replaceAll("\\\\", ""); // 移除转义斜杠
            scoreJsonString = scoreJsonString.replaceAll("\"\\{", "{"); // 移除双引号前的斜杠
            scoreJsonString = scoreJsonString.replaceAll("\\}\"", "}"); // 移除双引号后的斜杠

            // 解析为 JSON 对象
            Object scoreObject = mapper.readValue(scoreJsonString, Object.class);

            // 将 JSON 对象转换为格式化的字符串
            String scoreFormattedJson = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(scoreObject);
            JSONObject jsonObject = JSONObject.parseObject(scoreFormattedJson, JSONObject.class);
            JSONArray testScene = jsonObject.getJSONArray("testScene");
            JSONObject infoMap = new JSONObject();
            if (CollectionUtils.isNotEmpty(testScene)) {
                infoMap = testScene.getJSONObject(0).getJSONObject("info");
            }

            List<TaskDcVo> taskDcVos = taskDcMapper.selectDcByTaskId(taskId);

            for (TaskDcVo taskDcVo : CollectionUtils.emptyIfNull(taskDcVos)) {
                if (infoMap.containsKey("efficiency")) {
                    JSONArray efficiency = infoMap.getJSONArray("efficiency");
                    for (Object o : efficiency) {
                        JSONObject item = (JSONObject) o;
                        String index = DataUtils.convertUnicodeToChinese(item.getString("index"));
                        if (taskDcVo.getName().equals(index)) {
                            taskDcVo.setScore(item.getString("score"));
                            taskDcVo.setTime(item.getString("time"));
                        }
                    }
                }
                if (infoMap.containsKey("comfortable")) {
                    JSONArray comfortable = infoMap.getJSONArray("comfortable");
                    for (Object o : comfortable) {
                        JSONObject item = (JSONObject) o;
                        String index = DataUtils.convertUnicodeToChinese(item.getString("index"));
                        if (taskDcVo.getName().equals(index)) {
                            taskDcVo.setScore(item.getString("type") + ":" + item.getString("score"));
                            taskDcVo.setTime(item.getString("time"));
                        }
                    }
                }
                if (infoMap.containsKey("safe")) {
                    JSONArray safe = infoMap.getJSONArray("safe");
                    for (Object o : safe) {
                        JSONObject item = (JSONObject) o;
                        String index = DataUtils.convertUnicodeToChinese(item.getString("index"));
                        if (taskDcVo.getName().equals(index)) {
                            taskDcVo.setScore(item.getString("score"));
                            taskDcVo.setTime(item.getString("time"));
                        }
                    }
                }
                TjTaskDc tjTaskDc = new TjTaskDc();
                BeanUtils.copyProperties(taskDcVo, tjTaskDc);
                taskDcMapper.updateById(tjTaskDc);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updateRunningCase(String key, Integer caseId) {
        this.runningCase.put(key, caseId);
    }

    public Integer getRunningCase(String key) {
        if (!this.runningCase.containsKey(key)) {
            return null;
        }
        return this.runningCase.get(key);
    }

    public void clearRunningCase(String key) {
        this.runningCase.remove(key);
    }

    public void removeMessageListeners(String key) {
        if (!this.runningChannel.containsKey(key)) {
            return;
        }
        List<ChannelListener<SimulationTrajectoryDto>> channelListeners = runningChannel.get(key);
        List<ChannelTopic> topics = channelListeners.stream().map(ChannelListener::getChannel).map(ChannelTopic::new)
                .collect(Collectors.toList());
        log.info("removeMessageListeners:{}", JSONObject.toJSONString(topics));
        redisMessageListenerContainer.removeMessageListener(channelListeners.get(0).getListener(), topics);
    }

    public void receiveData(String key, String channel, SimulationTrajectoryDto data) {
        if (!this.runningChannel.containsKey(key)) {
            return;
        }
        for (ChannelListener<SimulationTrajectoryDto> channelListener : this.runningChannel.get(key)) {
            if (channelListener.getChannel().equals(channel)) {
                channelListener.refreshData(data);
            }
        }
    }

    public synchronized int getDataSize(String key, String channel) {
        if (!this.runningChannel.containsKey(key)) {
            return 0;
        }
        for (ChannelListener<SimulationTrajectoryDto> channelListener : this.runningChannel.get(key)) {
            if (channelListener.getChannel().equals(channel)) {
                return channelListener.getCurrentSize();
            }
        }
        return 0;
    }

    public void removeListener(String key) {
        if (!this.runningChannel.containsKey(key)) {
            return;
        }
        removeMessageListeners(key);
        this.runningChannel.remove(key);
    }

    public void save(String key, Integer recordId, CaseTrajectoryDetailBo originalTrajectory, Integer action) {
        try {
            List<RealTestTrajectoryDto> data = new ArrayList<>();
            List<ChannelListener<SimulationTrajectoryDto>> channelListeners = this.runningChannel.get(key);
            for (ChannelListener<SimulationTrajectoryDto> channelListener : channelListeners) {
                RealTestTrajectoryDto realTestTrajectoryDto = new RealTestTrajectoryDto();
                realTestTrajectoryDto.setChannel(channelListener.getChannel());
                realTestTrajectoryDto.setData(channelListener.getData());
                data.add(realTestTrajectoryDto);
            }
            routeService.saveTaskRouteFile(recordId, data, originalTrajectory, action);
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
    }


    /**
     * 移除所有过期监听器
     */
    public void removeListeners() {
        try {
            Iterator<Entry<String, List<ChannelListener<SimulationTrajectoryDto>>>> iterator =
                    this.runningChannel.entrySet().iterator();
            while (iterator.hasNext()) {
                Entry<String, List<ChannelListener<SimulationTrajectoryDto>>> next = iterator.next();
                List<ChannelListener<SimulationTrajectoryDto>> value = next.getValue();
                int expireCount = 0;
                for (ChannelListener<SimulationTrajectoryDto> channelListener : value) {
                    if (channelListener.isExpire()) {
                        expireCount++;
                    }
                }
                if (expireCount == value.size()) {
                    removeMessageListeners(next.getKey());
                    iterator.remove();  // Removes the current element from the map
                }
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    private ChannelListener<SimulationTrajectoryDto> getListener(String key, String channel) {
        if (runningChannel.containsKey(key)) {
            List<ChannelListener<SimulationTrajectoryDto>> channelListeners = this.runningChannel.get(key);
            Map<String, ChannelListener<SimulationTrajectoryDto>> listenerMap = channelListeners.stream()
                    .collect(Collectors.toMap(ChannelListener::getChannel, value -> value));
            return ObjectUtils.isEmpty(listenerMap) ? null : listenerMap.get(channel);
        }
        return null;
    }

    public static class ChannelListener<T> {
        private final String channel;
        private final String userName;
        private final String role;
        private Long timestamp;
        private final MessageListener listener;
        private final List<T> data;
        private boolean started;
        private boolean finished;

        public ChannelListener(String channel, String userName, String role, Long timestamp,
                               MessageListener listener) {
            this.channel = channel;
            this.userName = userName;
            this.role = role;
            this.timestamp = timestamp;
            this.listener = listener;
            this.data = new ArrayList<>();
            this.started = false;
            this.finished = false;
        }

        public void refreshData(T data) {
            this.data.add(data);
            this.timestamp = System.currentTimeMillis();
        }

        public boolean isExpire() {
            return System.currentTimeMillis() - timestamp > 10000;
        }

        public synchronized int getCurrentSize() {
            return this.data.size();
        }


        public String getChannel() {
            return channel;
        }


        public String getUserName() {
            return userName;
        }

        public String getRole() {
            return role;
        }

        public Long getTimestamp() {
            return timestamp;
        }


        public MessageListener getListener() {
            return listener;
        }

        public List<T> getData() {
            return data;
        }

        public boolean isFinished() {
            return finished;
        }

        public boolean isStarted() {
            return started;
        }

        public void finish() {
            this.finished = true;
        }

        public void start() {
            this.started = true;
        }

        public void end() {
            this.started = false;
        }

    }

}
