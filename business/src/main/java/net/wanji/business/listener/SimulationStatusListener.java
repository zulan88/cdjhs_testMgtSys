package net.wanji.business.listener;


import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import net.wanji.business.common.Constants;
import net.wanji.business.domain.dto.device.DeviceStateDto;
import net.wanji.common.core.redis.RedisCache;
import net.wanji.common.utils.RedisKeyUtils;
import net.wanji.common.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

/**
 * @author: jenny
 * @create: 2024-07-02 2:51 下午
 */
@Component
@Slf4j
public class SimulationStatusListener implements MessageListener {
    @Autowired
    private RedisCache redisCache;

    @Resource
    private RedisMessageListenerContainer redisMessageListenerContainer;

    private static String statusPatternTopic = StringUtils.format(Constants.ChannelBuilder.TESTING_CHANNEL_TEMPLATE, "*", "*", 0, Constants.ChannelBuilder.TASK, Constants.ChannelBuilder.STATUS_SUFFIX);

    @PostConstruct
    public void validChannel() {
        addSimulationStatusListener(statusPatternTopic);
    }

    private void addSimulationStatusListener(String statusPatternTopic) {
        redisMessageListenerContainer.addMessageListener(this, new PatternTopic(statusPatternTopic));
        log.info("添加仿真心跳监听器: {}", statusPatternTopic);
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
            log.info("接受到仿真准备状态: {}", body);
            String channelTopic = new String(message.getChannel());
            DeviceStateDto deviceStateDto = JSONObject.parseObject(body, DeviceStateDto.class);
            Integer deviceId = deviceStateDto.getDeviceId();
            Integer state = deviceStateDto.getState();
            String key = RedisKeyUtils.getSimulationPrepareStatusKey(deviceId, channelTopic);
            redisCache.setCacheObject(key, state, 5, TimeUnit.SECONDS);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
