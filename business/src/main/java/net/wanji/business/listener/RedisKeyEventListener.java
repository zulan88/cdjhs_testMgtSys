package net.wanji.business.listener;

import lombok.extern.slf4j.Slf4j;
import net.wanji.business.exercise.ExerciseHandler;
import net.wanji.common.utils.RedisKeyUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

/**
 * @author: jenny
 * @create: 2024-07-09 3:16 下午
 */
@Slf4j
@Component
public class RedisKeyEventListener implements MessageListener {

    @Resource
    private RedisMessageListenerContainer redisMessageListenerContainer;

    @Value("${spring.redis.database}")
    private Integer db;

    @PostConstruct
    public void init(){
        //监听指定db的key过期事件
        String pattern = "__keyevent@" + db + "__:expired";
        addRedisKeyEventListener(pattern);
    }

    private void addRedisKeyEventListener(String patternTopic) {
        redisMessageListenerContainer.addMessageListener(this, new ChannelTopic(patternTopic));
        log.info("添加过期key事件监听器: {}", patternTopic);
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        byte[] body = message.getBody();
        String key = new String(body);
        String statusPrefix = RedisKeyUtils.DEVICE_STATUS_PRE + RedisKeyUtils.DEVICE_STATUS_PRE_LINK;
        if(key.startsWith(statusPrefix)){
            String[] split = key.split(":");
            String uniques = split[1].trim();
            log.info("{}设备离线", uniques);
        }
    }
}
