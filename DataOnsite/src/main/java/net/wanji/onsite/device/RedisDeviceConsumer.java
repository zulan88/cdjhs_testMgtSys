package net.wanji.onsite.device;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonParser;
import io.netty.util.concurrent.DefaultThreadFactory;
import net.wanji.common.common.SimulationTrajectoryDto;
import net.wanji.common.core.redis.RedisCache;
import net.wanji.common.utils.SecurityUtils;
import net.wanji.common.utils.StringUtils;
import net.wanji.onsite.entity.TjOnsiteCase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.*;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * @Auther: guanyuduo
 * @Date: 2023/8/9 9:28
 * @Descriptoin:
 */
@Component
public class RedisDeviceConsumer {

    private static final Logger log = LoggerFactory.getLogger("redis");

    private final ConcurrentHashMap<String, ChannelListener<SimulationTrajectoryDto>> runningChannel = new ConcurrentHashMap<>();

    private ConcurrentHashMap<String, SimulationTrajectoryDto> lastTrajectory = new ConcurrentHashMap<>();

    private final RedisMessageListenerContainer redisMessageListenerContainer;

    private final SendData sendData;

    public RedisDeviceConsumer(RedisMessageListenerContainer redisMessageListenerContainer, SendData sendData) {
        this.redisMessageListenerContainer = redisMessageListenerContainer;
        this.sendData = sendData;
    }


    @Autowired
    private RedisCache redisCache;

    @Value("${domain.controll:30}")
    Integer domainControll;


    @PostConstruct
    public void validChannel() {
        ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(1,
                new DefaultThreadFactory("RedisTrajectory2Consumer-removeListeners"));
        scheduledExecutorService.scheduleAtFixedRate(
                this::removeListeners, 0, 10, TimeUnit.MINUTES);
    }

    /**
     * 在线调试
     *
     * @param onsiteCase
     */
    public void subscribeAndSend(TjOnsiteCase onsiteCase) {
        // 添加监听器
        this.addRunningChannel(onsiteCase);
    }


    /**
     * 添加监听器
     *
     * @param onsiteCase
     */
    public void addRunningChannel(TjOnsiteCase onsiteCase) {
        String channel = onsiteCase.getChannel();
        if (this.runningChannel.containsKey(channel)) {
            log.info("通道 {} 已存在", channel);
            return;
        }
        MessageListener listener = createListener(channel, onsiteCase);
        this.runningChannel.put(channel, new ChannelListener(onsiteCase.getOnsiteNumber(), channel, SecurityUtils.getUsername(),
                System.currentTimeMillis(), listener));
        redisMessageListenerContainer.addMessageListener(listener, new ChannelTopic(channel));
        log.info("添加监听器 {} 成功", channel);
    }



    /**
     * 创建监听器
     *
     * @param channel       通道名称
     * @param onsiteCase 调试参数
     * @return
     */
    public MessageListener createListener(String channel, TjOnsiteCase onsiteCase) {
        ObjectMapper objectMapper = new ObjectMapper();
        String methodLog = StringUtils.format("{}onsite模拟设备 - ", onsiteCase.getChannel());
        System.out.println(methodLog);
        String datachannel = channel.replace("control", "data");
        String statuschannel = channel.replace("control", "status");
        Long nowtime = System.currentTimeMillis();
        AtomicInteger deviceId = new AtomicInteger();
        return (message, pattern) -> {
            JSONObject jsonObject = JSONObject.parseObject(message.toString());
            if (jsonObject.getInteger("type").equals(0)){
                deviceId.set(jsonObject.getInteger("deviceId"));
                sendData.status(statuschannel, deviceId.get());
            }else if (jsonObject.getInteger("type").equals(1)){
                sendData.prepareData(datachannel, onsiteCase.getRoutefile());
                redisCache.publishMessage("STATUSResult", JSONObject.parseObject("{\"timestamp\": "+System.currentTimeMillis()/1000D+", \"deviceId\": "+deviceId.get()+", \"type\": 0, \"state\": 1}"));
                redisCache.publishMessage(statuschannel, JSONObject.parseObject("{\"timestamp\": "+System.currentTimeMillis()/1000D+", \"deviceId\": "+deviceId.get()+", \"caseId\": -999, \"type\": 1, \"state\": 1}"));
                sendData.status(statuschannel, deviceId.get());
            }else if (jsonObject.getInteger("type").equals(2)){
                JSONObject param = jsonObject.getJSONObject("params");
                if (param.getInteger("taskType").equals(1)){
                    String mainchannel = param.getJSONArray("protocols").getJSONObject(0).getJSONObject("params").getString("nearbyDataChannel");
                    sendData.startSendData(datachannel, mainchannel, onsiteCase.getCaseId());
                }else if (param.getInteger("taskType").equals(0)||param.getInteger("taskType").equals(-2)){
                    sendData.stopSendData(datachannel);
                }
            }

        };
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
