package net.wanji.business.service;

import net.wanji.business.domain.dto.device.DeviceStateDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

/**
 * @author: guanyuduo
 * @date: 2023/10/12 10:01
 * @descriptoin:
 */
@Service
public class DeviceStateSendService {

    @Autowired
    private RedisTemplate<String, Object> noClassRedisTemplate;

    public void sendData(String channel, DeviceStateDto t) {
        noClassRedisTemplate.convertAndSend(channel, t);
    }

}
