package net.wanji.business.listener;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import net.wanji.business.exercise.dto.ImageDelResultDto;
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
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * @author: jenny
 * @create: 2024-06-23 9:13 下午
 */
@Component
@Slf4j
public class ImageDelResultListener implements MessageListener {
    @Autowired
    private RedisCache redisCache;
    @Resource
    private RedisMessageListenerContainer redisMessageListenerContainer;

    @Value("${redis.channel.image.delete}")
    private String imageDelResultChannel;

    private static ConcurrentHashMap<String, CountDownLatch> latchMap = new ConcurrentHashMap<>();

    @PostConstruct
    public void validChannel() {
        addImageDelResultListener(imageDelResultChannel);
    }

    private void addImageDelResultListener(String imageDelResultChannel) {
        redisMessageListenerContainer.addMessageListener(this, new ChannelTopic(imageDelResultChannel));
        log.info("添加镜像删除结果监听器: {}", imageDelResultChannel);
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

            ImageDelResultDto imageDelResultDto = JSONObject.parseObject(body, ImageDelResultDto.class);
            String deviceId = imageDelResultDto.getDeviceId();
            String imageId = imageDelResultDto.getImageId();
            Integer imageStatus = imageDelResultDto.getImageStatus();
            String key = RedisKeyUtils.getImageDeleteResultKey(deviceId, imageId);
            redisCache.setCacheObject(key, imageStatus, 1, TimeUnit.DAYS);

            CountDownLatch latch = latchMap.get(deviceId);
            if(latch != null){
                latch.countDown();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public Integer awaitingMessage(String deviceId, String imageId, long timeout, TimeUnit timeUnit){
        CountDownLatch latch = new CountDownLatch(1);
        latchMap.put(deviceId, latch);
        try {
            boolean success = latch.await(timeout, timeUnit);
            latchMap.remove(deviceId);
            String key = RedisKeyUtils.getImageDeleteResultKey(deviceId, imageId);
            return success ? redisCache.getCacheObject(key) : null;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

}
