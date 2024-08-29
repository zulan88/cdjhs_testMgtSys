package net.wanji.business.exercise.dto.luansheng;

import lombok.extern.slf4j.Slf4j;
import net.wanji.business.exception.BusinessException;
import net.wanji.business.exercise.dto.evaluation.StartPoint;
import net.wanji.business.service.KafkaProducer;
import net.wanji.common.common.ClientSimulationTrajectoryDto;
import net.wanji.common.core.redis.RedisCache;
import net.wanji.framework.manager.AsyncManager;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ScheduledFuture;

/**
 * @author: jenny
 * @create: 2024-08-28 5:24 下午
 */
@Slf4j
public class TWPlaybackSchedule {
    private static Map<Long, ScheduledFuture<?>> futureMap = new HashMap<>(16);

    public static void startSendingData(Long taskId, String key, List<List<ClientSimulationTrajectoryDto>> trajectories, List<StartPoint> sceneStartPoints, Double radius, KafkaProducer kafkaProducer) throws IOException, BusinessException {
        stopSendingData(taskId);
        RealPlaybackDomainTW realPlaybackDomain = new RealPlaybackDomainTW(taskId, key, trajectories, sceneStartPoints, radius, kafkaProducer);
        ScheduledFuture<?> future = AsyncManager.me().execute(realPlaybackDomain, 0, 100);
        futureMap.put(taskId, future);
        log.info("创建孪生回放任务{}完成", taskId);
    }

    public static void stopSendingData(Long taskId) throws BusinessException, IOException {
        if (!futureMap.containsKey(taskId)) {
            return;
        }
        ScheduledFuture<?> future = futureMap.remove(taskId);
        if(future != null && !future.isCancelled()){
            future.cancel(true);
        }
        log.info("删除孪生回放任务{}", taskId);
    }
}
