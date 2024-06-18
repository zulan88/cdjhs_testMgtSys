package net.wanji.business.service;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import net.wanji.business.component.CountDownValueDto;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author: guanyuduo
 * @date: 2023/10/13 11:13
 * @descriptoin:
 */
@Component
@Slf4j
public class StatusManage {

    public static Map<String, CountDownValueDto> countDownLatchMap = new HashMap<>();

    public static void addCountDownLatch(String key, long time) throws InterruptedException {
        CountDownValueDto countDownValueDto = new CountDownValueDto();
        countDownLatchMap.put(key, countDownValueDto);
        boolean await = countDownValueDto.getCountDownLatch().await(time, TimeUnit.MILLISECONDS);
    }

    public static void countDown(String key, Object value) {
        CountDownValueDto countDownValueDto = countDownLatchMap.get(key);
        if (!ObjectUtils.isEmpty(countDownValueDto) && !ObjectUtils.isEmpty(countDownValueDto.getCountDownLatch())) {
            countDownValueDto.setValue(value);
            countDownValueDto.getCountDownLatch().countDown();
        }
    }

    public static Object getValue(String key) {
        try {
            CountDownValueDto countDownValueDto = countDownLatchMap.get(key);
            log.info("查询状态key：{}，value：{}", key, JSONObject.toJSONString(countDownValueDto));
            if (!ObjectUtils.isEmpty(countDownValueDto)) {
                return countDownValueDto.getValue();
            }
            return null;
        } finally {
            countDownLatchMap.remove(key);
        }
    }
}
