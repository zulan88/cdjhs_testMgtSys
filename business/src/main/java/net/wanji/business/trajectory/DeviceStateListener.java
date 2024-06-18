package net.wanji.business.trajectory;

import com.alibaba.fastjson2.JSONObject;
import lombok.extern.slf4j.Slf4j;
import net.wanji.business.component.DeviceReportFactory;
import net.wanji.business.service.DeviceReportService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

/**
 * @author glace
 * @version 1.0
 * @className DeviceStateConsumer
 * @description TODO
 * @date 2023/10/7 14:24
 **/
@Component
@Slf4j
public class DeviceStateListener implements MessageListener {
    @Resource
    private RedisTemplate<String, String> redisTemplate;

    @Resource
    private DeviceReportFactory deviceReportFactory;

    @Resource
    private RedisMessageListenerContainer redisMessageListenerContainer;

    @Value("${redis.channel.device.state}")
    private String deviceStateChannel;

    @PostConstruct
    public void validChannel() {
        addDeviceStateListener(deviceStateChannel);
    }


    @Override
    public void onMessage(Message message, byte[] pattern) {
        message.getChannel();
        Object object = redisTemplate.getValueSerializer()
                .deserialize(message.getBody());
        if (null == object) {
            if (log.isWarnEnabled()) {
                log.info("Report message is null!");
            }
            return;
        }
        JSONObject jsonObject = (JSONObject) object;
        jsonObject.put("channel", new String(message.getChannel()));
        DeviceReportService<Object> deviceReportService = deviceReportFactory.create(jsonObject.getInteger("type"));
        deviceReportService.dataProcess(jsonObject);
    }

    public void addDeviceStateListener(String stateChannel) {
        redisMessageListenerContainer.addMessageListener(this, new ChannelTopic(stateChannel));
        log.info("添加设备（准备）状态监听器：{}", stateChannel);
    }

    public void removeDeviceStateListener(String stateChannel) {
        redisMessageListenerContainer.removeMessageListener(this, new ChannelTopic(stateChannel));
        log.info("移除设备（准备）状态监听器：{}", stateChannel);
    }

}
