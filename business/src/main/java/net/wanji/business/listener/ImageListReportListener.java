package net.wanji.business.listener;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import net.wanji.business.exercise.dto.ImageListResultDto;
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
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * @author: jenny
 * @create: 2024-06-23 5:46 下午
 */
@Slf4j
@Component
public class ImageListReportListener implements MessageListener {
    @Autowired
    private RedisCache redisCache;
    @Resource
    private RedisMessageListenerContainer redisMessageListenerContainer;

    @Value("${redis.channel.image.report}")
    private String imageListResultChannel;

    private static ConcurrentHashMap<String, CountDownLatch> latchMap = new ConcurrentHashMap<>();

    @PostConstruct
    public void validChannel() {
        addImageListReportListener(imageListResultChannel);
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
            ImageListResultDto imageListResultDto = JSONObject.parseObject(body, ImageListResultDto.class);
            String deviceId = imageListResultDto.getDeviceId();
            List<String> imageList = imageListResultDto.getImageList();
            String key = RedisKeyUtils.getImageListReportKey(deviceId);
            redisCache.setCacheList(key, imageList);
            redisCache.expire(key, 1, TimeUnit.DAYS);

            CountDownLatch latch = latchMap.get(deviceId);
            if(latch != null){
                latch.countDown();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void addImageListReportListener(String imageListResultChannel) {
        redisMessageListenerContainer.addMessageListener(this, new ChannelTopic(imageListResultChannel));
        log.info("添加镜像列表获取监听器: {}", imageListResultChannel);
    }

    public List<String> awaitMessage(String deviceId, long timeout, TimeUnit timeUnit) {
        CountDownLatch latch = new CountDownLatch(1);
        latchMap.put(deviceId, latch);
        try {
            boolean success = latch.await(timeout, timeUnit);
            latchMap.remove(deviceId);
            String key = RedisKeyUtils.getImageListReportKey(deviceId);
            return success ? redisCache.getCacheList(key) : null;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }
}
