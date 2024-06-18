package net.wanji.business.trajectory;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.wanji.business.common.Constants.PartRole;
import net.wanji.business.common.Constants.RedisMessageType;
import net.wanji.business.component.PathwayPoints;
import net.wanji.business.domain.RealWebsocketMessage;
import net.wanji.business.domain.bo.CaseConfigBo;
import net.wanji.business.domain.bo.CaseInfoBo;
import net.wanji.business.domain.bo.CaseTrajectoryDetailBo;
import net.wanji.business.domain.bo.ParticipantTrajectoryBo;
import net.wanji.business.domain.bo.TrajectoryDetailBo;
import net.wanji.business.entity.TjCaseRealRecord;
import net.wanji.business.mapper.TjCaseRealRecordMapper;
import net.wanji.business.service.RouteService;
import net.wanji.business.socket.WebSocketManage;
import net.wanji.common.common.RealTestTrajectoryDto;
import net.wanji.common.common.SimulationMessage;
import net.wanji.common.common.SimulationTrajectoryDto;
import net.wanji.common.common.TrajectoryValueDto;
import net.wanji.common.utils.DataUtils;
import net.wanji.common.utils.DateUtils;
import net.wanji.common.utils.StringUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import java.util.TreeSet;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

/**
 * @Auther: guanyuduo
 * @Date: 2023/8/9 9:28
 * @Descriptoin:
 */
@Component
public class ImitateRedisTrajectoryConsumer {

    private static final Logger log = LoggerFactory.getLogger("business");

    private final ConcurrentHashMap<String, List<ChannelListener<SimulationTrajectoryDto>>> runningChannel = new ConcurrentHashMap<>();

    private final RedisMessageListenerContainer redisMessageListenerContainer;

    public ImitateRedisTrajectoryConsumer(RedisMessageListenerContainer redisMessageListenerContainer) {
        this.redisMessageListenerContainer = redisMessageListenerContainer;
    }

    @Autowired
    private RouteService routeService;

    @Autowired
    private TjCaseRealRecordMapper caseRealRecordMapper;

    @PostConstruct
    public void validChannel() {
//        ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(1,
//                new DefaultThreadFactory("ImitateRedisTrajectoryConsumer-removeListeners"));
//        scheduledExecutorService.scheduleAtFixedRate(
//                this::removeListeners, 0, 20, TimeUnit.SECONDS);
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


    /**
     * 订阅轨迹
     *
     * @param caseInfoBo 用例信息
     * @param key        wsKey
     * @param username   用户名
     * @return
     */
    public void subscribeAndSend(CaseInfoBo caseInfoBo, String key, String username) throws IOException {
        // 添加监听器
        this.addRunningChannel(caseInfoBo, key, username);
    }

    public void addRunningChannel(CaseInfoBo caseInfoBo, String key, String username) throws IOException {
        if (this.runningChannel.containsKey(key)) {
            log.info("通道 {} 已存在", key);
            return;
        }
        List<CaseConfigBo> caseConfigs = caseInfoBo.getCaseConfigs().stream().filter(info ->
                !ObjectUtils.isEmpty(info.getDeviceId())).collect(Collectors.collectingAndThen(
                Collectors.toCollection(() ->
                        new TreeSet<>(Comparator.comparing(CaseConfigBo::getDeviceId))), ArrayList::new));

        MessageListener listener = createListener(key, caseInfoBo.getCaseRealRecord(), caseConfigs);
        List<ChannelTopic> topics = caseConfigs.stream().map(CaseConfigBo::getDataChannel).map(ChannelTopic::new).collect(Collectors.toList());
        List<ChannelListener<SimulationTrajectoryDto>> listeners = new ArrayList<>();
        for (CaseConfigBo configBo : caseConfigs) {
            ChannelListener<SimulationTrajectoryDto> channelListener =
                    new ChannelListener<>(caseInfoBo.getId(), caseInfoBo.getCaseRealRecord().getId(),
                            configBo.getDataChannel(), username,
                            configBo.getSupportRoles(), System.currentTimeMillis(), listener);
            listeners.add(channelListener);
        }
        this.runningChannel.put(key, listeners);
        redisMessageListenerContainer.addMessageListener(listener, topics);
        log.info("添加监听器 {} 成功", JSON.toJSONString(topics));
    }

    /**
     * 创建监听器
     *
     * @param key            wskey
     * @param caseRealRecord 用例实车试验记录
     * @param configBos      用例实车试验配置
     * @return
     * @throws IOException
     */
    public MessageListener createListener(String key, TjCaseRealRecord caseRealRecord, List<CaseConfigBo> configBos) throws IOException {
        // 所有通道和业务车辆ID映射
        Map<String, List<CaseConfigBo>> allChannelAndBusinessIdMap = configBos.stream().collect(Collectors.groupingBy(CaseConfigBo::getDataChannel));
        // av配置
        List<CaseConfigBo> avConfigs = configBos.stream().filter(item -> PartRole.AV.equals(item.getSupportRoles())).collect(Collectors.toList());
        // av类型通道和业务车辆ID映射
        Map<String, String> avChannelAndBusinessIdMap = avConfigs.stream().collect(Collectors.toMap(
                CaseConfigBo::getDataChannel, CaseConfigBo::getBusinessId));
        // av类型通道和业务车辆名称映射
        Map<String, String> avChannelAndNameMap = configBos.stream().filter(item -> PartRole.AV.equals(item.getSupportRoles()))
                .collect(Collectors.toMap(CaseConfigBo::getDataChannel, CaseConfigBo::getName));

        CaseTrajectoryDetailBo originalTrajectory = JSONObject.parseObject(caseRealRecord.getDetailInfo(),
                CaseTrajectoryDetailBo.class);
        Map<String, List<TrajectoryDetailBo>> avBusinessIdPointsMap = originalTrajectory.getParticipantTrajectories()
                .stream().filter(item ->
                        avChannelAndBusinessIdMap.containsValue(item.getId())).collect(Collectors.toMap(
                        ParticipantTrajectoryBo::getId,
                        ParticipantTrajectoryBo::getTrajectory
                ));

        CaseConfigBo caseConfigBo = avConfigs.get(0);
        // 主车全部点位
        List<TrajectoryDetailBo> avPoints = avBusinessIdPointsMap.get(caseConfigBo.getBusinessId());
        // 主车全程、剩余时间、到达时间
        ArrayList<Point2D.Double> doubles = new ArrayList<>();
        for (TrajectoryDetailBo trajectoryDetailBo : avPoints) {
            String[] pos = trajectoryDetailBo.getPosition().split(",");
            doubles.add(new Point2D.Double(Double.parseDouble(pos[0]), Double.parseDouble(pos[1])));
        }
        // 主车途径点提示
        PathwayPoints pathwayPoints = new PathwayPoints(avPoints);
        // 读取仿真验证主车轨迹
        Map<String, Object> realMap = new HashMap<>();
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        String methodLog = StringUtils.format("{}实车验证 - ", caseRealRecord.getCaseId());

        return (message, pattern) -> {
            try {
                String channel = new String(message.getChannel());
                SimulationMessage simulationMessage = objectMapper.readValue(message.toString(), SimulationMessage.class);
                if (!ObjectUtils.isEmpty(simulationMessage.getValue()) && simulationMessage.getValue() instanceof LinkedHashMap) {
                    LinkedHashMap value = (LinkedHashMap) simulationMessage.getValue();
                    value.remove("@type");
                }
                ChannelListener<SimulationTrajectoryDto> channelListener = this.getListener(key, channel);
                if (ObjectUtils.isEmpty(channelListener)) {
                    log.error("查询监听器异常：{} - {}", key, channel);
                    return;
                }
                switch (simulationMessage.getType()) {
                    case RedisMessageType.TRAJECTORY:
                        SimulationTrajectoryDto simulationTrajectory = objectMapper.readValue(JSON.toJSONString(simulationMessage.getValue()), SimulationTrajectoryDto.class);
                        // 实际轨迹消息
                        List<TrajectoryValueDto> data = simulationTrajectory.getValue();
                        for (TrajectoryValueDto trajectoryValueDto : CollectionUtils.emptyIfNull(data)) {
                            trajectoryValueDto.setName(DataUtils.convertUnicodeToChinese(trajectoryValueDto.getName()));
                            allChannelAndBusinessIdMap.get(channel).stream().filter(item -> item.getName().equals(trajectoryValueDto.getName())).findFirst().ifPresent(item -> trajectoryValueDto.setId(item.getBusinessId()));
                        }
                        // 无论是否有轨迹都保存
                        receiveData(key, channel, simulationTrajectory);
                        if (CollectionUtils.isNotEmpty(data)) {
                            // send ws
                            String duration = DateUtils.secondsToDuration(
                                    (int) Math.floor((double) (getDataSize(key, channel)) / 10));
                            if (!ObjectUtils.isEmpty(avChannelAndBusinessIdMap)
                                    && avChannelAndBusinessIdMap.containsKey(channel)) {
                                PathwayPoints nearestPoint = pathwayPoints.findNearestPoint(data.get(0).getLongitude(), data.get(0).getLatitude());
                                Map<String, Object> tipsMap = new HashMap<>();
                                if (nearestPoint.hasTips()) {
                                    tipsMap.put("name", avChannelAndNameMap.get(channel));
                                    tipsMap.put("pointName", nearestPoint.getPointName());
                                    tipsMap.put("pointDistance", nearestPoint.getDistance());
                                    tipsMap.put("pointSpeed", nearestPoint.getPointSpeed());
                                }
                                realMap.put("tips", tipsMap);
                                // 仿真车未来轨迹
                                List<Map<String, Double>> futureList = new ArrayList<>();
                                realMap.put("simuFuture", futureList);
                                realMap.put("speed", data.get(0).getSpeed());
                                RealWebsocketMessage msg = new RealWebsocketMessage(RedisMessageType.TRAJECTORY, realMap, data,
                                        duration);
                                WebSocketManage.sendInfo(key.concat("_").concat(channel), JSON.toJSONString(msg));
                            } else {
                                RealWebsocketMessage msg = new RealWebsocketMessage(RedisMessageType.TRAJECTORY, null, data,
                                        duration);
                                WebSocketManage.sendInfo(key.concat("_").concat(channel), JSON.toJSONString(msg));
                            }
                        }
                        break;
                    case RedisMessageType.END:
                        log.info("收到结束消息：{}", key);
                        TjCaseRealRecord param = new TjCaseRealRecord();
                        param.setId(caseRealRecord.getId());
                        String duration = DateUtils.secondsToDuration(
                                (int) Math.floor((double) (getDataSize(key,
                                        caseConfigBo.getDataChannel())) / 10));
                        originalTrajectory.setDuration(duration);
                        param.setDetailInfo(JSON.toJSONString(originalTrajectory));
                        int endSuccess = caseRealRecordMapper.updateById(param);
                        log.info("修改用例场景评价:{}", endSuccess);
                        removeListener(key, true, originalTrajectory);

                        RealWebsocketMessage endMsg = new RealWebsocketMessage(RedisMessageType.END, null, null,
                                duration);
                        WebSocketManage.sendInfo(key.concat("_").concat(channel), JSON.toJSONString(endMsg));
                        break;
                    default:
                        break;
                }
            } catch (Exception e) {
                log.error("{} 实车试验数据接收异常：{}", key, e);
                removeListener(key, true, originalTrajectory);
            }
        };
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

    /**
     * 移除监听器
     *
     * @param key
     * @param save
     * @param originalTrajectory
     */
    public void removeListener(String key, boolean save, CaseTrajectoryDetailBo originalTrajectory) {
        if (!this.runningChannel.containsKey(key)) {
            return;
        }
        removeMessageListeners(key);
        List<RealTestTrajectoryDto> data = new ArrayList<>();
        List<ChannelListener<SimulationTrajectoryDto>> channelListeners = this.runningChannel.get(key);
        Integer recordId = channelListeners.get(0).getRecordId();
        for (ChannelListener<SimulationTrajectoryDto> channelListener : channelListeners) {
            RealTestTrajectoryDto realTestTrajectoryDto = new RealTestTrajectoryDto();
            realTestTrajectoryDto.setChannel(channelListener.getChannel());
            realTestTrajectoryDto.setData(channelListener.getData());
            realTestTrajectoryDto.setMain(StringUtils.isNotEmpty(channelListener.getRole())
                    && PartRole.AV.equals(channelListener.getRole()));
//            for (SimulationTrajectoryDto trajectoryDto : channelListener.getData()) {
//                routeService.checkRealRoute(recordId, originalTrajectory, trajectoryDto.getValue());
//            }
            data.add(realTestTrajectoryDto);
        }
        if (save) {
            try {
                routeService.saveRealRouteFile(recordId, data);
            } catch (ExecutionException | InterruptedException e) {
                e.printStackTrace();
            }
        }
        this.runningChannel.remove(key);
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

    public void removeMessageListeners(String key) {
        if (!this.runningChannel.containsKey(key)) {
            return;
        }
        List<ChannelListener<SimulationTrajectoryDto>> channelListeners = runningChannel.get(key);
        List<ChannelTopic> topics = channelListeners.stream().map(ChannelListener::getChannel).map(ChannelTopic::new)
                .collect(Collectors.toList());
        log.info("removeMessageListeners:{}", JSON.toJSONString(topics));
        redisMessageListenerContainer.removeMessageListener(channelListeners.get(0).getListener(), topics);
    }

    public static class ChannelListener<T> {
        private final Integer caseId;
        private final Integer recordId;
        private final String channel;
        private final String userName;
        private final String role;
        private Long timestamp;
        private final MessageListener listener;
        private final List<T> data;
        private final boolean finished;
        private boolean started;

        public ChannelListener(Integer caseId, Integer recordId, String channel, String userName, String role, Long timestamp,
                               MessageListener listener) {
            this.caseId = caseId;
            this.recordId = recordId;
            this.channel = channel;
            this.userName = userName;
            this.role = role;
            this.timestamp = timestamp;
            this.listener = listener;
            this.data = new ArrayList<>();
            this.finished = false;
            this.started = false;
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

        public Integer getCaseId() {
            return caseId;
        }

        public Integer getRecordId() {
            return recordId;
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

        public void start() {
            this.started = true;
        }

    }

}
