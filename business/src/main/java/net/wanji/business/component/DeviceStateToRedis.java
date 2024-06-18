package net.wanji.business.component;

import net.wanji.common.utils.StringUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.Set;

/**
 * @author glace
 * @version 1.0
 * @className DeviceStateToRedis
 * @description TODO
 * @date 2023/10/7 16:31
 **/
@Component
public class DeviceStateToRedis {
  private static final Logger log = LoggerFactory.getLogger("redis");

  public static final String DEVICE_STATE_PREFIX = "deviceState";
  public static final String DEVICE_READY_STATE_PREFIX = "deviceReadyState";

  private final RedisTemplate<String, Integer> redisTemplate;

  public DeviceStateToRedis(RedisTemplate redisTemplate) {
    this.redisTemplate = redisTemplate;
  }

  public void save(Integer deviceId, Integer state, String prefix, String suffix) {
    redisTemplate.opsForValue().set(getKey(deviceId, prefix, suffix), state,
        Duration.of(1500, ChronoUnit.MILLIS));
  }

  public void save(Integer deviceId, String prefix, String suffix) {
    save(deviceId, 1, prefix, suffix);
  }

  public Integer query(Integer deviceId, String prefix, String suffix) {
    return redisTemplate.opsForValue().get(getKey(deviceId, prefix, suffix));
  }

  public void delete(Integer deviceId, String prefix) {
    Set<String> keys = redisTemplate.keys(prefix + "_" + deviceId);
    if (CollectionUtils.isNotEmpty(keys)) {
      redisTemplate.delete(keys);
      log.info("删除redis设备准备状态 keys: {}", StringUtils.join(keys, ","));
    }
  }

  private static String getKey(Integer deviceId, String prefix, String suffix) {
    return prefix + "_" + deviceId + "_" + suffix;
  }
}
