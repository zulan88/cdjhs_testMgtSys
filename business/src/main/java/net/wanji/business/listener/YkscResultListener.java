package net.wanji.business.listener;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import net.wanji.business.exercise.TimeoutConfig;
import net.wanji.business.exercise.dto.YkscResultDto;
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
 * @author: jenny
 * @create: 2024-07-31 10:45 上午
 */
@Component
@Slf4j
public class YkscResultListener implements MessageListener {
    @Autowired
    private RedisCache redisCache;

    @Resource
    private RedisMessageListenerContainer redisMessageListenerContainer;

    @Autowired
    private TimeoutConfig timeoutConfig;

    @Value("${redis.channel.yksc}")
    private String ykscResultChannel;

    @PostConstruct
    public void validChannel() {
        addYkscResultListener(ykscResultChannel);
    }

    public void addYkscResultListener(String ykscResultChannel) {
        redisMessageListenerContainer.addMessageListener(this, new ChannelTopic(ykscResultChannel));
        log.info("添加实车测试域控插件上报监听器: {}", ykscResultChannel);
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
            YkscResultDto ykscResultDto = JSONObject.parseObject(body, YkscResultDto.class);
            String deviceId = ykscResultDto.getDeviceId();
            String key = RedisKeyUtils.getCdjhsYkscResultKey(deviceId);
            redisCache.setCacheObject(key, ykscResultDto, timeoutConfig.deviceStatus, TimeUnit.SECONDS);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
