package net.wanji.business.exercise.dto.luansheng;

import lombok.extern.slf4j.Slf4j;
import net.wanji.common.common.Constants;
import net.wanji.common.common.TrajectoryValueDto;
import net.wanji.common.core.redis.RedisCache;
import net.wanji.common.utils.RedisKeyUtils;
import net.wanji.common.utils.spring.SpringUtils;
import org.springframework.data.redis.core.ZSetOperations;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * @author: jenny
 * @create: 2024-08-29 8:49 上午
 */
@Slf4j
public class StatCache {
    private static RedisCache redisCache = SpringUtils.getBean("redisCache");

    public static void update(Integer taskId, TrajectoryValueDto dto, CommonField object){
        try {
            String statKey = RedisKeyUtils.getCdjhsLuanshengStatKey(taskId);
            String globalTimeStamp = dto.getGlobalTimeStamp();
            long currentTimestamp = Long.parseLong(globalTimeStamp) / 1000;
            Long lastTimestamp = object.getLastTimestamp();
            if(Objects.isNull(lastTimestamp) || (currentTimestamp - lastTimestamp == 1)) {
                if (Objects.isNull(dto.getLonAcc())) {
                    dto.setLonAcc(0.0);
                }
                if (Objects.isNull(dto.getLatAcc())) {
                    dto.setLatAcc(0.0);
                }
                if (Objects.isNull(dto.getAngularVelocityX())) {
                    dto.setAngularVelocityX(0.0);
                }
                object.setLastTimestamp(currentTimestamp);
                if (redisCache.hasKey(statKey)) {
                    //计算横向加加速度和纵向加加速度
                    Set<ZSetOperations.TypedTuple<TrajectoryValueDto>> last = redisCache.revRangeWithScores(statKey, 0, 0);
                    ZSetOperations.TypedTuple<TrajectoryValueDto> tuple = new ArrayList<>(last).get(0);
                    Double lastScore = tuple.getScore();
                    assert lastScore != null;
                    long preTimestamp = lastScore.longValue();
                    TrajectoryValueDto lastValue = tuple.getValue();
                    assert lastValue != null;
                    dto.setLonAcc2((dto.getLonAcc() - lastValue.getLonAcc()) / (currentTimestamp - preTimestamp));
                    dto.setLatAcc2((dto.getLatAcc() - lastValue.getLatAcc()) / (currentTimestamp - preTimestamp));
                }
                if(Objects.isNull(dto.getLonAcc2())){
                    dto.setLonAcc2(0.0);
                }
                if(Objects.isNull(dto.getLatAcc2())){
                    dto.setLatAcc2(0.0);
                }
                //计算超出阈值时长
                String thresoldKey = RedisKeyUtils.getCdjhsLuanshengStatThresoldKey(taskId);
                StatResult statResult = redisCache.getCacheObject(thresoldKey);
                if (Objects.isNull(statResult)) {
                    statResult = new StatResult();
                }
                StatThresoldEnum[] enums = StatThresoldEnum.values();
                for (StatThresoldEnum thresoldEnum : enums) {
                    String name = thresoldEnum.getName();
                    double[] thresold = thresoldEnum.getThresold();
                    try {
                        Field field = dto.getClass().getDeclaredField(name);
                        field.setAccessible(true);
                        double value = Double.parseDouble(field.get(dto).toString());
                        //是否超出阈值
                        String isExceedFieldName = name + Constants.Exceed_Limit;
                        Field isExceedField = object.getClass().getSuperclass().getDeclaredField(isExceedFieldName);
                        isExceedField.setAccessible(true);
                        boolean isExceed = (Boolean) isExceedField.get(object);
                        //超出阈值开始时间
                        String startTimeFiledName = name + Constants.Start_Time;
                        Field startTimeField = object.getClass().getSuperclass().getDeclaredField(startTimeFiledName);
                        startTimeField.setAccessible(true);
                        Long startTime = (Long) startTimeField.get(object);
                        //超出阈值时长
                        String sumFieldName = name + Constants.OVER_LIMIT;
                        Field sumField = statResult.getClass().getDeclaredField(sumFieldName);
                        sumField.setAccessible(true);
                        int sum = (Integer) sumField.get(statResult);
                        if (value < thresold[0] || value > thresold[1]) {
                            if (!isExceed) {
                                isExceedField.set(object, true);
                                startTimeField.set(object, currentTimestamp);
                            }
                        } else {
                            if (isExceed) {
                                isExceedField.set(object, false);
                            }
                        }
                        isExceed = (Boolean) isExceedField.get(object);
                        if(isExceed){
                            sum += currentTimestamp - startTime;
                            sumField.set(statResult, sum);
                        }
                    } catch (NoSuchFieldException | IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
                redisCache.setCacheObject(thresoldKey, statResult);
                redisCache.expire(thresoldKey, 30, TimeUnit.MINUTES);
                redisCache.zAdd(statKey, dto, currentTimestamp);
                if (redisCache.zCard(statKey) > 31) {
                    redisCache.remove(statKey, 0, 0);
                }
                redisCache.expire(statKey, 30, TimeUnit.MINUTES);
            }
        }catch (Exception e){
            log.info("更新任务{}图表缓存报错", taskId, e);
        }
    }
}
