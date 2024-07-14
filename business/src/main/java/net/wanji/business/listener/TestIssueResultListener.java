package net.wanji.business.listener;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import net.wanji.business.exercise.dto.TestIssueResultDto;
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
 * @create: 2024-06-23 10:32 下午
 */
@Component
@Slf4j
public class TestIssueResultListener implements MessageListener {
    @Autowired
    private RedisCache redisCache;
    @Resource
    private RedisMessageListenerContainer redisMessageListenerContainer;

    @Value("${redis.channel.test.issue}")
    private String testIssueResultChannel;

    private static ConcurrentHashMap<String, CountDownLatch> latchMap = new ConcurrentHashMap<>();

    @PostConstruct
    public void validChannel() {
        addTestIssueResultListener(testIssueResultChannel);
    }

    private void addTestIssueResultListener(String testIssueResultChannel) {
        redisMessageListenerContainer.addMessageListener(this, new ChannelTopic(testIssueResultChannel));
        log.info("添加练习任务下发结果监听器: {}", testIssueResultChannel);
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
            TestIssueResultDto testIssueResultDto = JSONObject.parseObject(body, TestIssueResultDto.class);
            String deviceId = testIssueResultDto.getDeviceId();
            log.info("接收到设备{}上报练习任务下发结果:{}", deviceId, body);
            String key = RedisKeyUtils.getTestIssueResultKey(deviceId);
            redisCache.setCacheObject(key, testIssueResultDto, 10, TimeUnit.SECONDS);

            CountDownLatch latch = latchMap.get(deviceId);
            if(latch != null){
                latch.countDown();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public TestIssueResultDto awaitingMessage(String deviceId, long timeout, TimeUnit timeUnit) throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);
        latchMap.put(deviceId, latch);
        boolean success = latch.await(timeout, timeUnit);
        latchMap.remove(deviceId);
        String key = RedisKeyUtils.getTestIssueResultKey(deviceId);
        return success ? redisCache.getCacheObject(key) : null;
    }
}
