package net.wanji.business.listener;

import lombok.extern.slf4j.Slf4j;
import net.wanji.business.common.Constants;
import net.wanji.business.service.KafkaProducer;
import net.wanji.common.core.redis.RedisCache;
import net.wanji.common.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

/**
 * @author: jenny
 * @create: 2024-07-13 18:54
 */
@Slf4j
@Component
public class SimulationDataListener implements MessageListener {
    @Autowired
    private RedisCache redisCache;

    @Resource
    private RedisMessageListenerContainer redisMessageListenerContainer;

    @Autowired
    private KafkaProducer kafkaProducer;

    private static String dataPatternTopic = StringUtils.format(Constants.ChannelBuilder.TESTING_CHANNEL_TEMPLATE, "*", "*", 0, Constants.ChannelBuilder.TASK, Constants.ChannelBuilder.DATA_SUFFIX);

    @PostConstruct
    public void validChannel() {
        addSimulationDataListener(dataPatternTopic);
    }

    private void addSimulationDataListener(String dataPatternTopic) {
        redisMessageListenerContainer.addMessageListener(this, new PatternTopic(dataPatternTopic));
        log.info("添加仿真背景车数据监听器: {}", dataPatternTopic);
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
            String channelTopic = new String(message.getChannel());
            String kafkaTopic = StringUtils.format(Constants.ChannelBuilder.CDJHS_TESS_DATA_TOPIC_TEMPLATE, channelTopic);
            kafkaProducer.sendMessage(kafkaTopic, body);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
