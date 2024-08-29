package net.wanji.business.exercise.dto.luansheng;

import net.wanji.business.domain.dto.ToLocalDto;
import net.wanji.common.common.TrajectoryValueDto;
import net.wanji.common.core.redis.RedisCache;
import net.wanji.common.utils.RedisKeyUtils;
import net.wanji.common.utils.spring.SpringUtils;
import org.springframework.data.redis.core.ZSetOperations;

import java.util.ArrayList;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * @author: jenny
 * @create: 2024-08-29 8:49 上午
 */
public class StatCache {
    private static RedisCache redisCache = SpringUtils.getBean("redisCache");

    public static void update(Integer taskId, TrajectoryValueDto dto, Object object){
        if(!(object instanceof RealPlaybackDomainTW) && !(object instanceof ToLocalDto)){
            return;
        }
        String statKey = RedisKeyUtils.getCdjhsLuanshengStatKey(taskId);
        String globalTimeStamp = dto.getGlobalTimeStamp();
        long currentTimestamp = Long.parseLong(globalTimeStamp) / 1000;
        Long lastTimestamp;
        if(object instanceof RealPlaybackDomainTW){
            RealPlaybackDomainTW value = (RealPlaybackDomainTW) object;
            lastTimestamp = value.getLastTimestamp();
        }else{
            ToLocalDto value = (ToLocalDto) object;
            lastTimestamp = value.getLastTimestamp();
        }
        if(Objects.isNull(lastTimestamp) || (currentTimestamp - lastTimestamp == 1)){
            if(object instanceof RealPlaybackDomainTW){
                RealPlaybackDomainTW value = (RealPlaybackDomainTW) object;
                value.setLastTimestamp(currentTimestamp);
            }else{
                ToLocalDto value = (ToLocalDto)object;
                value.setLastTimestamp(currentTimestamp);
            }
            if(redisCache.hasKey(statKey)){
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
            redisCache.zAdd(statKey, dto, currentTimestamp);
            if(redisCache.zCard(statKey) > 31){
                redisCache.remove(statKey, 0, 0);
            }
            redisCache.expire(statKey, 30, TimeUnit.MINUTES);
        }
    }
}
