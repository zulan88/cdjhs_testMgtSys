package net.wanji.business.trajectory;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.netty.util.concurrent.DefaultThreadFactory;
import net.wanji.business.common.Constants.ChannelBuilder;
import net.wanji.business.common.Constants.Extension;
import net.wanji.business.common.Constants.PartType;
import net.wanji.business.common.Constants.RedisMessageType;
import net.wanji.business.domain.InElement;
import net.wanji.business.domain.InfinteMileScenceExo;
import net.wanji.business.domain.WebsocketMessage;
import net.wanji.business.domain.bo.CaseTrajectoryDetailBo;
import net.wanji.business.domain.bo.ParticipantTrajectoryBo;
import net.wanji.business.domain.bo.TrajectoryDetailBo;
import net.wanji.business.domain.dto.SceneDebugDto;
import net.wanji.business.domain.vo.ParticipantTrajectoryVo;
import net.wanji.business.entity.InfinteMileScence;
import net.wanji.business.entity.TjFragmentedSceneDetail;
import net.wanji.business.exception.BusinessException;
import net.wanji.business.service.InfinteMileScenceService;
import net.wanji.business.service.RouteService;
import net.wanji.business.service.TjFragmentedSceneDetailService;
import net.wanji.business.socket.WebSocketManage;
import net.wanji.common.common.SimulationMessage;
import net.wanji.common.common.SimulationOptimizeDto;
import net.wanji.common.common.SimulationTrajectoryDto;
import net.wanji.common.common.TrajectoryValueDto;
import net.wanji.common.config.WanjiConfig;
import net.wanji.common.core.redis.RedisCache;
import net.wanji.common.utils.DateUtils;
import net.wanji.common.utils.SecurityUtils;
import net.wanji.common.utils.StringUtils;
import net.wanji.common.utils.bean.BeanUtils;
import net.wanji.common.utils.file.FileUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.text.ParseException;
import java.util.*;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * @Auther: guanyuduo
 * @Date: 2023/8/9 9:28
 * @Descriptoin:
 */
@Component
public class RedisTrajectory2Consumer {

    private static final Logger log = LoggerFactory.getLogger("redis");

    private final ConcurrentHashMap<String, ChannelListener<SimulationTrajectoryDto>> runningChannel = new ConcurrentHashMap<>();

    private ConcurrentHashMap<String, SimulationTrajectoryDto> lastTrajectory = new ConcurrentHashMap<>();

    private final RedisMessageListenerContainer redisMessageListenerContainer;

    public RedisTrajectory2Consumer(RedisMessageListenerContainer redisMessageListenerContainer) {
        this.redisMessageListenerContainer = redisMessageListenerContainer;
    }

    @Autowired
    private RouteService routeService;

    @Autowired
    private RedisCache redisCache;

    @Value("${domain.controll:30}")
    Integer domainControll;

    @Autowired
    private InfinteMileScenceService infinteMileScenceService;

    @Autowired
    private TjFragmentedSceneDetailService tjFragmentedSceneDetailService;

    @PostConstruct
    public void validChannel() {
        ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(1,
                new DefaultThreadFactory("RedisTrajectory2Consumer-removeListeners"));
        scheduledExecutorService.scheduleAtFixedRate(
                this::removeListeners, 0, 60, TimeUnit.SECONDS);
    }

    /**
     * 在线调试
     *
     * @param sceneDebugDto
     */
    public void subscribeAndSend(SceneDebugDto sceneDebugDto) {
        // 添加监听器
        this.addRunningChannel(sceneDebugDto);
    }


    /**
     * 添加监听器
     *
     * @param sceneDebugDto
     */
    public void addRunningChannel(SceneDebugDto sceneDebugDto) {
        String channel = ChannelBuilder.buildSimulationChannel(SecurityUtils.getUsername(), sceneDebugDto.getNumber());
        if (this.runningChannel.containsKey(channel)) {
            log.info("通道 {} 已存在", channel);
            return;
        }
        MessageListener listener = createListener(channel, sceneDebugDto);
        this.runningChannel.put(channel, new ChannelListener(sceneDebugDto.getNumber(), channel, SecurityUtils.getUsername(),
                System.currentTimeMillis(), listener));
        redisMessageListenerContainer.addMessageListener(listener, new ChannelTopic(channel));
        log.info("添加监听器 {} 成功", channel);
    }

    public void addRunningChannelInfinite(InfinteMileScenceExo infinteMileScenceExo) {
        String channel = ChannelBuilder.buildInfiniteSimulationChannel(SecurityUtils.getUsername(), infinteMileScenceExo.getViewId());
        if (this.runningChannel.containsKey(channel)) {
            log.info("通道 {} 已存在", channel);
            return;
        }
        MessageListener listener = createListenerInfinite(channel, infinteMileScenceExo);
        this.runningChannel.put(channel, new ChannelListener(infinteMileScenceExo.getViewId(), channel, SecurityUtils.getUsername(),
                System.currentTimeMillis(), listener));
        redisMessageListenerContainer.addMessageListener(listener, new ChannelTopic(channel));
        log.info("添加监听器 {} 成功", channel);
    }


    /**
     * 创建监听器
     *
     * @param channel       通道名称
     * @param sceneDebugDto 调试参数
     * @return
     */
    public MessageListener createListener(String channel, SceneDebugDto sceneDebugDto) {
        ObjectMapper objectMapper = new ObjectMapper();
        String methodLog = StringUtils.format("{}仿真验证 - ", sceneDebugDto.getNumber());
        Long nowtime = System.currentTimeMillis();
        List<String> mainId = new ArrayList<>();
        sceneDebugDto.getTrajectoryJson().getParticipantTrajectories().stream().filter(p -> PartType.MAIN.equals(p.getType())).findFirst().ifPresent(p -> {
            mainId.add(p.getId());
        });
        final boolean[] isOne = {true};
        return (message, pattern) -> {
            try {
                // 解析消息
                SimulationMessage simulationMessage = objectMapper.readValue(
                        message.toString(),
                        SimulationMessage.class);
//                log.info(StringUtils.format("{}收到消息：{}", methodLog, JSONObject.toJSONString(simulationMessage)));
                // 计时
                String duration = DateUtils.secondsToDuration(
                        (int) Math.ceil((double) (getDataSize(channel)) / 10));
                ChannelListener<SimulationTrajectoryDto> channelListener = this.runningChannel.get(channel);
                switch (simulationMessage.getType()) {
                    // 开始消息
                    case RedisMessageType.START:
                        log.info(StringUtils.format("{}开始", methodLog));
                        channelListener.start();
                        break;
                    // 轨迹消息
                    case RedisMessageType.TRAJECTORY:
                        if (!channelListener.started) {
                            break;
                        }
                        String optimize = String.valueOf(simulationMessage.getValue()).replace("NaN", "0");
                        SimulationTrajectoryDto simulationTrajectory = objectMapper.readValue(optimize, SimulationTrajectoryDto.class);
                        if (CollectionUtils.isNotEmpty(simulationTrajectory.getValue())) {
                            // 实际轨迹消息
                            List<TrajectoryValueDto> data = simulationTrajectory.getValue();
                            // 检查轨迹
                            List<ParticipantTrajectoryVo> res = routeService.checkSimulaitonRoute2(sceneDebugDto.getTrajectoryJson(), data, nowtime);
                            // 保存轨迹(本地)
                            for (TrajectoryValueDto value : data) {
                                if (mainId.contains(value.getId())) {
                                    value.setTimestamp(value.getTimestamp() + nowtime);
                                }
                            }
                            if (isOne[0]) {
                                isOne[0] = false;
                            }else {
                                receiveDataOld(channel, simulationTrajectory);
                            }
                            // send ws
                            WebsocketMessage msg = new WebsocketMessage(
                                    RedisMessageType.TRAJECTORY,
                                    duration,
                                    data);
                            msg.setObjlist(res);
                            WebSocketManage.sendInfo(channel, JSONObject.toJSONString(msg));
                        }
                        break;
                    case RedisMessageType.OPTIMIZE:
                        log.info("{}接收到轨迹优化消息：{}", methodLog, String.valueOf(simulationMessage.getValue()));
                        handleOptimize(String.valueOf(simulationMessage.getValue()));
                        break;
                    // 结束消息
                    case RedisMessageType.END:
                        if (!channelListener.started) {
                            break;
                        }
                        if (StringUtils.isNotEmpty(sceneDebugDto.getRouteFile())) {
                            FileUtils.deleteFile(sceneDebugDto.getRouteFile());
                        }
                        try {
                            String path = FileUtils.writeRoute(getData(channel), WanjiConfig.getRoutePath(), Extension.TXT);
                            log.info("routeFile:{}", path);
                            sceneDebugDto.setRouteFile(path);
                        } catch (Exception e) {
                            log.error("保存轨迹文件失败：{}", e);
                        }
                        // 移除监听器
                        removeListener(channel);
                        String repeatKey = "DEBUGGING_SCENE_" + sceneDebugDto.getNumber();
                        redisCache.deleteObject(repeatKey);
                        // 解析消息
                        CaseTrajectoryDetailBo end = objectMapper.readValue(String.valueOf(simulationMessage.getValue()),
                                CaseTrajectoryDetailBo.class);
                        log.info(StringUtils.format("{}结束：{}", methodLog, JSONObject.toJSONString(end)));
                        // 更新数据
                        Optional.ofNullable(end.getEvaluationVerify()).ifPresent(sceneDebugDto.getTrajectoryJson()::setEvaluationVerify);
                        sceneDebugDto.getTrajectoryJson().setDuration(duration);
                        // send ws
                        WebsocketMessage msg = new WebsocketMessage(RedisMessageType.END, null, sceneDebugDto);
//                        if(sceneDebugDto.getId()!=null){
//                            tjFragmentedSceneDetailService.saveSceneDebug(sceneDebugDto);
//                        }
                        WebSocketManage.sendInfo(channel, JSONObject.toJSONString(msg));
                        break;
                    default:
                        break;
                }
            } catch (IOException e) {
                log.error("解析消息失败：{}", e);
                removeListener(channel);
            } catch (ParseException e) {
                throw new RuntimeException(e);
            } catch (Exception e) {
                log.error("其他错误：{}", e);
            }
        };
    }

    public MessageListener createListenerInfinite(String channel, InfinteMileScenceExo infinteMileScenceExo) {
        ObjectMapper objectMapper = new ObjectMapper();
        String methodLog = StringUtils.format("{}仿真验证 - ", infinteMileScenceExo.getViewId());
        Long nowtime = System.currentTimeMillis();
        Map<String, Boolean> mainId = new HashMap<>();
        infinteMileScenceExo.getInElements().forEach(p -> {
            mainId.put(p.getId().toString(),true);
        });
        Map<String, Integer> mainCounting = mainId.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, p -> 34));
        AtomicInteger count = new AtomicInteger(60);
        List<InElement> toinElements = infinteMileScenceExo.getInElements();
        AtomicBoolean isValid = new AtomicBoolean(false);
        return (message, pattern) -> {
            try {
                // 解析消息
                SimulationMessage simulationMessage = objectMapper.readValue(
                        message.toString(),
                        SimulationMessage.class);
//                log.info(StringUtils.format("{}收到消息：{}", methodLog, JSONObject.toJSONString(simulationMessage)));
                // 计时
                String duration = DateUtils.secondsToDuration(
                        (int) Math.ceil((double) (getDataSize(channel)) / 10));
                ChannelListener<SimulationTrajectoryDto> channelListener = this.runningChannel.get(channel);
                switch (simulationMessage.getType()) {
                    // 开始消息
                    case RedisMessageType.START:
                        log.info(StringUtils.format("{}开始", methodLog));
                        channelListener.start();
                        break;
                    // 轨迹消息
                    case RedisMessageType.TRAJECTORY:
                        count.getAndDecrement();
                        if (!channelListener.started) {
                            break;
                        }
                        if (count.intValue()>=55){
                            break;
                        }
                        // 获取时间
//                        long start = System.currentTimeMillis();
                        String optimize = String.valueOf(simulationMessage.getValue()).replace("NaN", "0");
                        SimulationTrajectoryDto simulationTrajectory = objectMapper.readValue(optimize, SimulationTrajectoryDto.class);
                        if (CollectionUtils.isNotEmpty(simulationTrajectory.getValue())) {
                            // 实际轨迹消息
                            List<TrajectoryValueDto> data = simulationTrajectory.getValue();

                            List<InElement> inElements = toinElements.stream().filter(p -> {
                                if (mainId.containsKey(p.getId().toString())) {
                                    return true;
                                }
                                return false;
                            }).collect(Collectors.toList());

                            if(count.intValue()<=0){
                                routeService.checkinfinite(mainId,data,inElements);
                            }

                            List<TrajectoryValueDto> save = new ArrayList<>();

                            // 保存轨迹(本地)
                            for (TrajectoryValueDto value : data) {
                                if (mainCounting.containsKey(value.getId())) {
                                    if (!mainId.get(value.getId()) && mainCounting.get(value.getId()) > 0){
                                        mainCounting.put(value.getId(),mainCounting.get(value.getId()) - 1);
                                    }
                                    if(mainCounting.get(value.getId()) > 0) {
                                        value.setTimestamp(value.getTimestamp() + nowtime);
                                        save.add(value);
                                    }
                                }
                            }
                            if (CollectionUtils.isNotEmpty(save)) {
                                simulationTrajectory.setValue(save);
                                receiveData(channel, simulationTrajectory);
                            }else {
                                channelListener.refresh();
                            }

                            // send ws
                            WebsocketMessage msg = new WebsocketMessage(
                                    RedisMessageType.TRAJECTORY,
                                    duration,
                                    data);

                            AtomicBoolean canStop = new AtomicBoolean(true);
                            mainCounting.forEach((k,v)->{
                                if(v > 0){
                                    canStop.set(false);
                                }
                            });
                            msg.setCanStop(canStop.get());
                            isValid.set(canStop.get());

                            WebSocketManage.sendInfo(channel, JSONObject.toJSONString(msg));
                        }
//                        long end = System.currentTimeMillis();
//                        log.info(StringUtils.format("{}处理轨迹消息耗时：{}", methodLog, end - start));
                        break;
                    case RedisMessageType.OPTIMIZE:
                        log.info("{}接收到轨迹优化消息：{}", methodLog, String.valueOf(simulationMessage.getValue()));
                        handleOptimize(String.valueOf(simulationMessage.getValue()));
                        break;
                    // 结束消息
                    case RedisMessageType.END:
                        if (!channelListener.started) {
                            break;
                        }
                        if (StringUtils.isNotEmpty(infinteMileScenceExo.getRouteFile())) {
                            FileUtils.deleteFile(infinteMileScenceExo.getRouteFile());
                        }
                        try {
                            String path = FileUtils.writeRoute(getData(channel), WanjiConfig.getRoutePath(), Extension.TXT);
                            log.info("routeFile:{}", path);
                            infinteMileScenceExo.setRouteFile(path);
                        } catch (Exception e) {
                            log.error("保存轨迹文件失败：{}", e);
                        }
                        // 移除监听器
                        removeListener(channel);
                        String repeatKey = "DEBUGGING_INSCENE_" + infinteMileScenceExo.getViewId();
                        redisCache.deleteObject(repeatKey);

                        log.info(StringUtils.format("{}结束：{}", methodLog, JSONObject.toJSONString(simulationMessage)));

                        if (isValid.get()) {
                            InfinteMileScence infinteMileScence = new InfinteMileScence();
                            infinteMileScence.setRouteFile(infinteMileScenceExo.getRouteFile());
                            infinteMileScence.setId(infinteMileScenceExo.getId());
                            infinteMileScenceService.updateById(infinteMileScence);
                        }

                        // send ws
//                        WebsocketMessage msg = new WebsocketMessage(RedisMessageType.END, null, infinteMileScenceExo);
//                        WebSocketManage.sendInfo(channel, JSONObject.toJSONString(msg));
                        break;
                    default:
                        break;
                }
            } catch (Exception e) {
                log.error("解析消息失败：{}", e);
                removeListener(channel);
            }
        };
    }

    public void handleOptimize(String optimizeInfo) {
        JSONObject jsonObject = JSONObject.parseObject(optimizeInfo);
        List<SimulationOptimizeDto> list = new ArrayList<>();
        for (Entry<String, Object> entry : jsonObject.entrySet()) {
            JSONObject points = JSONObject.parseObject((String) entry.getValue());
            for (Entry<String, Object> detail : points.entrySet()) {
                list.add(new SimulationOptimizeDto(entry.getKey(), detail.getKey(), detail.getValue()));
            }
        }
        Map<String, List<SimulationOptimizeDto>> result = list.stream()
                .collect(Collectors.groupingBy(SimulationOptimizeDto::getId));
        log.info("优化结果：{}", JSONObject.toJSONString(result));
    }



    /**
     * 接收数据(包含插帧)
     *
     * @param key
     * @param data
     */
    public void receiveData(String key, SimulationTrajectoryDto data) {
        if (!this.runningChannel.containsKey(key)) {
            return;
        }
        if (data == null || data.getValue() == null || data.getValue().size() == 0) {
            return;
        }
        ChannelListener<SimulationTrajectoryDto> channelListener = this.runningChannel.get(key);
        if (lastTrajectory.containsKey(key)){
            SimulationTrajectoryDto lastData = lastTrajectory.get(key);
            SimulationTrajectoryDto sup = new SimulationTrajectoryDto();
            sup.setTimestamp(data.getTimestamp());
            sup.setTimestampType(data.getTimestampType());
            sup.setValue(new ArrayList<>());
            boolean flag = false;
            int index = 0;
            if (lastData.getValue().size() == data.getValue().size()) {
                for (TrajectoryValueDto frame : data.getValue()) {
                    double difference = Math.abs(frame.getCourseAngle() - lastData.getValue().get(index).getCourseAngle());
                    if (difference > 5) {
                        double longdiff = Math.abs(frame.getLongitude() - lastData.getValue().get(index).getLongitude());
                        double latdiff = Math.abs(frame.getLatitude() - lastData.getValue().get(index).getLatitude());
                        TrajectoryValueDto newFrame1 = new TrajectoryValueDto();
                        TrajectoryValueDto newFrame2 = new TrajectoryValueDto();
                        BeanUtils.copyProperties(frame, newFrame1);
                        BeanUtils.copyProperties(frame, newFrame2);
                        newFrame1.setCourseAngle(Math.min(frame.getCourseAngle(), lastData.getValue().get(index).getCourseAngle()) + (difference * 1 / 3));
                        newFrame1.setLatitude(Math.min(frame.getLatitude(), lastData.getValue().get(index).getLatitude()) + (latdiff * 1 / 3));
                        newFrame1.setLongitude(Math.min(frame.getLongitude(), lastData.getValue().get(index).getLongitude()) + (longdiff * 1 / 3));
                        newFrame1.setFrameId(-1);
                        sup.getValue().add(newFrame1);
                        newFrame2.setCourseAngle(Math.max(frame.getCourseAngle(), lastData.getValue().get(index).getCourseAngle()) - (difference * 1 / 3));
                        newFrame2.setLatitude(Math.max(frame.getLatitude(), lastData.getValue().get(index).getLatitude()) - (latdiff * 1 / 3));
                        newFrame2.setLongitude(Math.max(frame.getLongitude(), lastData.getValue().get(index).getLongitude()) - (longdiff * 1 / 3));
                        newFrame2.setFrameId(-1);
                        sup.getValue().add(newFrame2);
                        flag = true;
                    } else if (difference > 2) {
                        TrajectoryValueDto newFrame = new TrajectoryValueDto();
                        BeanUtils.copyProperties(frame, newFrame);
                        newFrame.setCourseAngle(Math.round(100 * (frame.getCourseAngle() + lastData.getValue().get(index).getCourseAngle()) / 2) / 100.0);
                        newFrame.setLatitude(Math.round(10000000 * (frame.getLatitude() + lastData.getValue().get(index).getLatitude()) / 2) / 10000000.0);
                        newFrame.setLongitude(Math.round(10000000 * (frame.getLongitude() + lastData.getValue().get(index).getLongitude()) / 2) / 10000000.0);
                        newFrame.setFrameId(-1);
                        sup.getValue().add(newFrame);
                        flag = true;
                    }
                    index++;
                }
                if (flag) {
                    channelListener.refreshData(sup);
                }
            }
        }
        lastTrajectory.put(key, data);
        channelListener.refreshData(data);
    }

    public void receiveDataOld(String key, SimulationTrajectoryDto data) {
        if (!this.runningChannel.containsKey(key)) {
            return;
        }
        if (data == null || data.getValue() == null || data.getValue().size() == 0) {
            return;
        }
        ChannelListener<SimulationTrajectoryDto> channelListener = this.runningChannel.get(key);
        channelListener.refreshData(data);
    }

    public List<SimulationTrajectoryDto> getData(String key) {
        if (!this.runningChannel.containsKey(key)) {
            return null;
        }
        ChannelListener<SimulationTrajectoryDto> channelListener = this.runningChannel.get(key);
        return channelListener.getData();
    }

    /**
     * 获取数据大小
     *
     * @param key
     * @return
     */
    public synchronized int getDataSize(String key) {
        if (!this.runningChannel.containsKey(key)) {
            return 0;
        }
        ChannelListener<SimulationTrajectoryDto> channelListener = this.runningChannel.get(key);
        return channelListener.getCurrentSize();
    }

    /**
     * 移除监听器
     *
     * @param channel
     */
    public void removeListener(String channel) {
        if (!this.runningChannel.containsKey(channel)) {
            return;
        }
        ChannelListener<SimulationTrajectoryDto> channelListener = this.runningChannel.get(channel);
        redisMessageListenerContainer.removeMessageListener(channelListener.getListener(), new ChannelTopic(channel));
        this.runningChannel.remove(channel);
    }

    /**
     * 移除过期监听器
     */
    public void removeListeners() {
        try {
            Iterator<Map.Entry<String, ChannelListener<SimulationTrajectoryDto>>> iterator =
                    this.runningChannel.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<String, ChannelListener<SimulationTrajectoryDto>> entry = iterator.next();
                ChannelListener<SimulationTrajectoryDto> channelListener = entry.getValue();
                if (channelListener.isExpire()) {
                    redisMessageListenerContainer.removeMessageListener(channelListener.getListener(),
                            new ChannelTopic(channelListener.getChannel()));
                    iterator.remove();  // Removes the current element from the map
                }
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    /**
     * 监听器实体类
     *
     * @param <T>
     */
    public static class ChannelListener<T> implements MessageListener {
        private final String sceneNumber;
        private final String channel;
        private boolean started = false;
        private final String userName;
        private Long timestamp;
        private final MessageListener listener;
        private final List<T> data;

        public ChannelListener(String sceneNumber, String channel, String userName, Long timestamp,
                               MessageListener listener) {
            this.sceneNumber = sceneNumber;
            this.channel = channel;
            this.started = false;
            this.userName = userName;
            this.timestamp = timestamp;
            this.listener = listener;
            this.data = new ArrayList<>();
        }

        public void refreshData(T data) {
            this.data.add(data);
            this.timestamp = System.currentTimeMillis();
        }

        public void refresh() {
            this.timestamp = System.currentTimeMillis();
        }

        public boolean isExpire() {
            return System.currentTimeMillis() - timestamp > 10000;
        }

        public int getCurrentSize() {
            return this.data.size();
        }

        public String getNumber() {
            return sceneNumber;
        }

        public String getChannel() {
            return channel;
        }


        public String getUserName() {
            return userName;
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

        public boolean isStarted() {
            return started;
        }

        public void start() {
            this.started = true;
        }

        @Override
        public void onMessage(Message message, byte[] pattern) {

        }
    }

}
