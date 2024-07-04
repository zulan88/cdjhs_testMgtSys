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
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

/**
 * @author: jenny
 * @create: 2024-07-03 4:21 下午
 */
@Component
@Slf4j
public class SimulationListener implements MessageListener {
    @Autowired
    private RedisCache redisCache;

    @Resource
    private RedisMessageListenerContainer redisMessageListenerContainer;

    private static String topic = Constants.ChannelBuilder.DEFAULT_STATUS_CHANNEL;

    @PostConstruct
    public void validChannel() {
        addSimulationStatusListener(topic);
    }

    private void addSimulationStatusListener(String topic) {
        redisMessageListenerContainer.addMessageListener(this, new ChannelTopic(topic));
        log.info("添加仿真状态监听器: {}", topic);
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
            DeviceStateDto deviceStateDto = JSONObject.parseObject(body, DeviceStateDto.class);
            Integer deviceId = deviceStateDto.getDeviceId();
            if(deviceId == 4){
                log.info("接收到设备-{}上报的状态:{}", deviceId, body);
            }
            Integer state = deviceStateDto.getState();
            String simulationStatusKey = RedisKeyUtils.getSimulationStatusKey(deviceId);
            redisCache.setCacheObject(simulationStatusKey, state, 5, TimeUnit.SECONDS);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
