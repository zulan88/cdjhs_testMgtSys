package net.wanji.business.trajectory;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.netty.util.concurrent.DefaultThreadFactory;
import net.wanji.business.common.Constants.Extension;
import net.wanji.business.common.Constants.RedisMessageType;
import net.wanji.business.domain.WebsocketMessage;
import net.wanji.business.domain.bo.CaseTrajectoryDetailBo;
import net.wanji.business.entity.TjTask;
import net.wanji.business.mapper.TjTaskMapper;
import net.wanji.business.socket.WebSocketManage;
import net.wanji.common.common.SimulationMessage;
import net.wanji.common.common.SimulationTrajectoryDto;
import net.wanji.common.common.TrajectoryValueDto;
import net.wanji.common.config.WanjiConfig;
import net.wanji.common.core.redis.RedisCache;
import net.wanji.common.utils.DateUtils;
import net.wanji.common.utils.SecurityUtils;
import net.wanji.common.utils.StringUtils;
import net.wanji.common.utils.file.FileUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @Auther: guanyuduo
 * @Date: 2023/8/9 9:28
 * @Descriptoin:
 */
@Component
public class RoutingPlanConsumer {

    private static final Logger log = LoggerFactory.getLogger("redis");

    private final ConcurrentHashMap<String, ChannelListener<SimulationTrajectoryDto>> runningChannel = new ConcurrentHashMap<>();

    private final RedisMessageListenerContainer redisMessageListenerContainer;

    public RoutingPlanConsumer(RedisMessageListenerContainer redisMessageListenerContainer) {
        this.redisMessageListenerContainer = redisMessageListenerContainer;
    }

    @Autowired
    private RedisCache redisCache;

    @Autowired
    private TjTaskMapper taskMapper;

    @PostConstruct
    public void validChannel() {
        ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(1,
                new DefaultThreadFactory("RoutingPlanConsumer-removeListeners"));
        scheduledExecutorService.scheduleAtFixedRate(
                this::removeListeners, 0, 60, TimeUnit.SECONDS);
    }

    /**
     * 路线优化
     *
     * @param taskId
     * @param taskCode
     */
    public void subscribeAndSend(String routingChannel, Integer taskId, String taskCode) {
        // 添加监听器
        this.addRunningChannel(routingChannel, taskId, taskCode);
    }


    /**
     * 添加监听器
     * @param routingChannel
     * @param taskId
     * @param taskCode
     */
    public void addRunningChannel(String routingChannel, Integer taskId, String taskCode) {
        if (this.runningChannel.containsKey(routingChannel)) {
            log.info("通道 {} 已存在", routingChannel);
            return;
        }
        MessageListener listener = createListener(routingChannel, taskId, taskCode);
        this.runningChannel.put(routingChannel, new ChannelListener(routingChannel, SecurityUtils.getUsername(),
                System.currentTimeMillis(), listener));
        redisMessageListenerContainer.addMessageListener(listener, new ChannelTopic(routingChannel));
        log.info("添加监听器 {} 成功", routingChannel);
    }


    /**
     * 创建监听器
     *
     * @param routingChannel  通道名称
     * @param taskId   调试参数
     * @param taskCode 调试参数
     * @return
     */
    public MessageListener createListener(String routingChannel, Integer taskId, String taskCode) {
        ObjectMapper objectMapper = new ObjectMapper();
        String methodLog = StringUtils.format("{}多场景路径优化 - ", taskCode);
        TjTask task = taskMapper.selectById(taskId);
        return (message, pattern) -> {
            try {
                // 解析消息
                SimulationMessage simulationMessage = objectMapper.readValue(
                        message.toString(),
                        SimulationMessage.class);
//                log.info(StringUtils.format("{}收到消息：{}", methodLog, JSONObject.toJSONString(simulationMessage)));
                // 计时
                String duration = DateUtils.secondsToDuration(
                        (int) Math.ceil((double) (getDataSize(routingChannel)) / 10));
                ChannelListener<SimulationTrajectoryDto> channelListener = this.runningChannel.get(routingChannel);
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
                            // 保存轨迹(本地)
                            receiveData(routingChannel, simulationTrajectory);
                            // send ws
                            WebsocketMessage msg = new WebsocketMessage(
                                    RedisMessageType.TRAJECTORY,
                                    duration,
                                    data);
                            WebSocketManage.sendInfo(routingChannel, JSONObject.toJSONString(msg));
                        }
                        break;
                    // 结束消息
                    case RedisMessageType.END:
                        if (!channelListener.started) {
                            break;
                        }
                        if (StringUtils.isNotEmpty(task.getRouteFile())) {
                            FileUtils.deleteFile(task.getRouteFile());
                        }
                        Map<String, Object> result = new HashMap<>();
                        try {
                            String path = FileUtils.writeRoute(getData(routingChannel), WanjiConfig.getRoutePath(), Extension.TXT);
                            log.info("routeFile:{}", path);
                            result.put("routeFile", path);
                        } catch (Exception e) {
                            log.error("保存轨迹文件失败：{}", e);
                        }
                        // 移除监听器
                        removeListener(routingChannel);
                        String repeatKey = "ROUTING_TASK_" + taskId;
                        redisCache.deleteObject(repeatKey);
                        // 解析消息
                        CaseTrajectoryDetailBo end = objectMapper.readValue(String.valueOf(simulationMessage.getValue()),
                                CaseTrajectoryDetailBo.class);
                        log.info(StringUtils.format("{}结束：{}", methodLog, JSONObject.toJSONString(end)));
                        // todo 连接线异常提示
                        result.put("success", true);
                        result.put("message", "无异常");
                        // send ws
                        WebsocketMessage msg = new WebsocketMessage(RedisMessageType.END, null, result);
                        WebSocketManage.sendInfo(routingChannel, JSONObject.toJSONString(msg));
                        break;
                    default:
                        break;
                }
            } catch (IOException e) {
                log.error("解析消息失败：{}", e);
                removeListener(routingChannel);
            }
        };
    }

    /**
     * 接收数据
     *
     * @param key
     * @param data
     */
    public void receiveData(String key, SimulationTrajectoryDto data) {
        if (!this.runningChannel.containsKey(key)) {
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
            Iterator<Entry<String, ChannelListener<SimulationTrajectoryDto>>> iterator =
                    this.runningChannel.entrySet().iterator();
            while (iterator.hasNext()) {
                Entry<String, ChannelListener<SimulationTrajectoryDto>> entry = iterator.next();
                ChannelListener<SimulationTrajectoryDto> channelListener = entry.getValue();
                if (channelListener.isExpire()) {
                    redisMessageListenerContainer.removeMessageListener(channelListener.getListener(),
                            new ChannelTopic(channelListener.getRoutingChannel()));
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
        private final String routingChannel;
        private boolean started;
        private final String userName;
        private Long timestamp;
        private final MessageListener listener;
        private final List<T> data;

        public ChannelListener(String routingChannel, String userName, Long timestamp,
                               MessageListener listener) {
            this.routingChannel = routingChannel;
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

        public boolean isExpire() {
            return System.currentTimeMillis() - timestamp > 10000;
        }

        public int getCurrentSize() {
            return this.data.size();
        }

        public String getRoutingChannel() {
            return routingChannel;
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
