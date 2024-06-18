package net.wanji.business.evaluation;

import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author hcy
 * @version 1.0
 * @className EvaluationRedisData
 * @description TODO
 * @date 2024/3/22 14:17
 **/
@Component
public class EvaluationRedisData implements MessageListener {
  /**
   * 可以加个定时器，异步线程唤醒检测，超时未处理数据剔除订阅者，待做！！！
   */
  private final Map<String, RedisChannelDataProcessor> subscribers = new HashMap<>();
  private final Map<String, List<String>> channelDataIds = new HashMap<>();
  private final Map<String, EvalContext> channelContextMap = new HashMap<>();

  private final RedisMessageListenerContainer redisMessageListenerContainer;
  @Resource
  private RedisTemplate<String, String> redisTemplate;

  public EvaluationRedisData(
      RedisMessageListenerContainer redisMessageListenerContainer) {
    this.redisMessageListenerContainer = redisMessageListenerContainer;
  }

  @Override
  public void onMessage(Message message, byte[] pattern) {
    Object data = redisTemplate.getValueSerializer()
        .deserialize(message.getBody());
    String channel = new String(message.getChannel());
    process(channel, data);
  }

  private void process(String channel, Object data) {
    RedisChannelDataProcessor processor = subscribers.get(channel);
    EvalContext evalContext = channelContextMap.get(channel);
    if (null != processor) {
      List<String> ids = channelDataIds.get(channel);
      if (null != ids && !ids.isEmpty()) {
        if (ids.contains(processor.dataId(data))) {
          processor.data(data, evalContext);
          processor.data(channel, data, evalContext);
        }
      } else {
        processor.data(data, evalContext);
        processor.data(channel, data, evalContext);
      }
    }
  }

  public void subscribe(String channel, RedisChannelDataProcessor processor,
      EvalContext context) {
    subscribe(channel, null, processor, context);
  }

  // 取消订阅
  public void unsubscribe(String channel) {
    unsubscribe(channel, null);
  }

  // 订阅
  public void subscribe(String channel, String dataId,
      RedisChannelDataProcessor processor, EvalContext context) {
    redisMessageListenerContainer.addMessageListener(this,
        new ChannelTopic(channel));
    subscribeRecord(channel, dataId, processor, context);
  }

  public void unsubscribe(String channel, String dataId) {
    clearSubscribeRecord(channel, dataId);
    redisMessageListenerContainer.removeMessageListener(this,
        new ChannelTopic(channel));
  }

  private void subscribeRecord(String channel, String dataId,
      RedisChannelDataProcessor processor, EvalContext context) {
    subscribers.put(channel, processor);
    if (null != dataId) {
      List<String> ids = new ArrayList<>();
      ids.add(dataId);
      channelDataIds.putIfAbsent(channel, ids);
    }
    if (null != context) {
      channelContextMap.put(channel, context);
    }
  }

  private void clearSubscribeRecord(String channel, String dataId) {
    if (null != dataId) {
      List<String> ids = channelDataIds.get(channel);
      if (null != ids) {
        ids.remove(dataId);
        if (ids.isEmpty()) {
          channelDataIds.remove(channel);
        }
      }
    }
    channelContextMap.remove(channel);
    subscribers.remove(channel);
  }

}
