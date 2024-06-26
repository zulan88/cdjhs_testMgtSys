package net.wanji.business.listener;

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

    @Override
    public void onMessage(Message message, byte[] pattern) {
        try {
            String channel = new String(message.getChannel());
            String trajectory = new String(message.getBody());
            LinkedBlockingQueue<String> queue = queueMap.get(channel);
            if (null == queue) {
                add(channel, new LinkedBlockingQueue<>(100));
            }
            queueMap.get(channel).add(trajectory);
        } catch (Exception e) {
            if (log.isErrorEnabled()) {
                log.error("data receive error!", e);
            }
        }
    }

    public void add(String channel, LinkedBlockingQueue<String> queue) {
        log.info("--------------- channel info start ---------------");
        log.info("add channel[{}] running channel ", channel);
        queueMap.forEach((k, v) -> log.info("channel[{}] size [{}]", channel, v.size()));
        log.info("---------------- channel info end ----------------");
        queueMap.put(channel, queue);
    }

    public void remove(String channel) {
        LinkedBlockingQueue<String> trajectories = queueMap.get(channel);
        queueMap.remove(channel);
        trajectories.clear();
        trajectories = null;
    }

    public LinkedBlockingQueue<String> getQueue(String channel) {
        return queueMap.get(channel);
    }
}
