package net.wanji.business.listener;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import net.wanji.business.exercise.dto.ImageIssueResultDto;
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
 * @create: 2024-06-23 9:58 下午
 */
@Component
@Slf4j
public class ImageIssueResultListener implements MessageListener {
    @Autowired
    private RedisCache redisCache;
    @Resource
    private RedisMessageListenerContainer redisMessageListenerContainer;

    @Value("${redis.channel.image.issue}")
    private String imageIssueResultChannel;

    private static ConcurrentHashMap<String, CountDownLatch> latchMap = new ConcurrentHashMap<>();

    @PostConstruct
    public void validChannel() {
        addImageIssueResultListener(imageIssueResultChannel);
    }

    private void addImageIssueResultListener(String imageIssueResultChannel) {
        redisMessageListenerContainer.addMessageListener(this, new ChannelTopic(imageIssueResultChannel));
        log.info("添加镜像下发结果监听器: {}", imageIssueResultChannel);
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
            ImageIssueResultDto imageIssueResultDto = JSONObject.parseObject(body, ImageIssueResultDto.class);
            String deviceId = imageIssueResultDto.getDeviceId();
            log.info("设备{}上报镜像下发结果: {}", deviceId, body);
            String imageId = imageIssueResultDto.getImageId();
            String key = RedisKeyUtils.getImageIssueResultKey(deviceId, imageId);
            redisCache.setCacheObject(key, imageIssueResultDto, 10, TimeUnit.SECONDS);

            CountDownLatch latch = latchMap.get(deviceId);
            if(latch != null){
                latch.countDown();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public ImageIssueResultDto awaitingMessage(String deviceId, String imageId, long timeout, TimeUnit timeUnit){
        CountDownLatch latch = new CountDownLatch(1);
        latchMap.put(deviceId, latch);
        try {
            boolean success = latch.await(timeout, timeUnit);
            latchMap.remove(deviceId);
            String key = RedisKeyUtils.getImageIssueResultKey(deviceId, imageId);
            return success ? redisCache.getCacheObject(key) : null;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }
}
