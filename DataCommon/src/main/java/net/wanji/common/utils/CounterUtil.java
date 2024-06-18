package net.wanji.common.utils;

import java.security.SecureRandom;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @Auther: guanyuduo
 * @Date: 2023/6/29 16:18
 * @Descriptoin:
 */

public class CounterUtil {

    private static int MAX_VALUE = 9999;
    private static Map<String, AtomicInteger> counterMap = new HashMap<>();
    private static DecimalFormat df = new DecimalFormat("0000");

    static {
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        long initialDelay = ChronoUnit.SECONDS.between(LocalDateTime.now(), LocalDateTime.of(LocalDateTime.now().plusDays(1).toLocalDate(), LocalTime.MIDNIGHT));
        scheduler.scheduleAtFixedRate(() -> counterMap.clear(), initialDelay, TimeUnit.DAYS.toSeconds(1),
                TimeUnit.SECONDS);
    }

    /**
     * 获取一个长度为4的由0和一个递增整数组成的字符串
     *
     * @return
     */
    public static synchronized String getNextNumber(String type) {
        if (!counterMap.containsKey(type)) {
            counterMap.put(type, new AtomicInteger(1));
        }
        int currentCounter = counterMap.get(type).getAndIncrement();
        if (currentCounter > MAX_VALUE) {
            resetCounter(type);
            currentCounter = counterMap.get(type).getAndIncrement();
        }
        return df.format(currentCounter);
    }

    private static void resetCounter(String type) {
        if (counterMap.containsKey(type)) {
            AtomicInteger counter = counterMap.get(type);
            counter.set(1);
        }
    }

    // 获取6位随机大写字母
    public static String getRandomChar() {
        StringBuilder result = new StringBuilder();
//        Random random = new Random();
        SecureRandom random = new SecureRandom();
        for (int i = 0; i < 4; i++) {
            result.append((char) (random.nextInt(26) + 65));
        }
        return result.toString();
    }
}
