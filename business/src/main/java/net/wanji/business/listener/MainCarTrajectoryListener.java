package net.wanji.business.listener;

import ch.qos.logback.classic.Logger;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * @author: jenny
 * @create: 2024-06-25 6:02 下午
 */
@Slf4j
public class MainCarTrajectoryListener implements MessageListener {
    private static Map<String, LinkedBlockingQueue<String>> queueMap = new ConcurrentHashMap<>();
    private static Map<String, Logger> loggerMap = new ConcurrentHashMap<>();

    @Override
    public void onMessage(Message message, byte[] pattern) {
        try {
            String channel = new String(message.getChannel());
            String trajectory = new String(message.getBody());
            Logger logger = loggerMap.get(channel);
            if(null != logger){
                logger.info(trajectory);
            }
            LinkedBlockingQueue<String> queue = queueMap.get(channel);
            if (null == queue) {
                queueMap.put(channel, new LinkedBlockingQueue<>(100));
            }
            queueMap.get(channel).add(trajectory);
        } catch (Exception e) {
            if (log.isErrorEnabled()) {
                log.error("data receive error!", e);
            }
        }
    }

    public void add(String channel, LinkedBlockingQueue<String> queue, Logger logger) {
        log.info("--------------- channel info start ---------------");
        log.info("add channel[{}] running channel ", channel);
        log.info("---------------- channel info end ----------------");
        queueMap.put(channel, queue);
        loggerMap.put(channel, logger);
    }

    public void remove(String channel) {
        LinkedBlockingQueue<String> trajectories = queueMap.get(channel);
        queueMap.remove(channel);
        trajectories.clear();
        trajectories = null;

        loggerMap.remove(channel);
    }

    public LinkedBlockingQueue<String> getQueue(String channel) {
        return queueMap.get(channel);
    }
}
