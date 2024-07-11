package net.wanji.business.trajectory;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import net.wanji.business.domain.dto.device.DeviceStateDto;
import net.wanji.business.exercise.ExerciseHandler;
import net.wanji.business.exercise.TimeoutConfig;
import net.wanji.common.core.redis.RedisCache;
import net.wanji.common.utils.RedisKeyUtils;
import net.wanji.common.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

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
    @Autowired
    private RedisCache redisCache;
    @Resource
    private RedisMessageListenerContainer redisMessageListenerContainer;

    @Autowired
    private TimeoutConfig timeoutConfig;

    @Value("${redis.channel.device.state}")
    private String deviceStateChannel;

    @PostConstruct
    public void validChannel() {
        addDeviceStateListener(deviceStateChannel);
    }


    @Override
    public void onMessage(Message message, byte[] pattern) {
        try {
            String body = new String(message.getBody());
            if (StringUtils.isEmpty(body)) {
                if (log.isWarnEnabled()) {
                    log.info("Report message is null!");
                }
                return;
            }
            DeviceStateDto stateDto = JSONObject.parseObject(body, DeviceStateDto.class);
            String uniques = stateDto.getUniques();
            Integer state = stateDto.getState();
            String key = RedisKeyUtils.getDeviceStatusKey(uniques);
            redisCache.setCacheObject(key, state, timeoutConfig.deviceStatus, TimeUnit.SECONDS);
            if(state == 2){
                //准备状态
                String readyKey = RedisKeyUtils.getDeviceReadyStatusKey(uniques);
                redisCache.setCacheObject(readyKey, state, timeoutConfig.deviceStatus, TimeUnit.SECONDS);
                ExerciseHandler.idleDeviceMap.put(uniques, state);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void addDeviceStateListener(String stateChannel) {
        redisMessageListenerContainer.addMessageListener(this, new ChannelTopic(stateChannel));
        log.info("添加设备（准备）状态监听器: {}", stateChannel);
    }

    public void removeDeviceStateListener(String stateChannel) {
        redisMessageListenerContainer.removeMessageListener(this, new ChannelTopic(stateChannel));
        log.info("移除设备（准备）状态监听器: {}", stateChannel);
    }

}
