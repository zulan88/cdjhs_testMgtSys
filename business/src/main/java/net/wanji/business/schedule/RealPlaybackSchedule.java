package net.wanji.business.schedule;

import net.wanji.business.exception.BusinessException;
import net.wanji.business.socket.WebSocketManage;
import net.wanji.common.common.ClientSimulationTrajectoryDto;
import net.wanji.common.common.RealTestTrajectoryDto;
import net.wanji.common.common.SimulationTrajectoryDto;
import net.wanji.common.common.TrajectoryValueDto;
import net.wanji.common.utils.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

/**
 * @Auther: guanyuduo
 * @Date: 2023/7/10 18:41
 * @Descriptoin:
 */

public class RealPlaybackSchedule {

    private static final Logger log = LoggerFactory.getLogger("business");

    static Map<String, RealPlaybackDomain> futureMap = new HashMap<>(16);


    public static void startSendingData(String key, String mainChannel, List<List<ClientSimulationTrajectoryDto>> trajectories) throws BusinessException, IOException {
        stopSendingData(key);
        RealPlaybackDomain realPlaybackDomain = new RealPlaybackDomain(key, mainChannel, trajectories);
        futureMap.put(key, realPlaybackDomain);
        log.info("创建实车回放任务{}完成", key);
    }

    public static void suspend(String key) throws BusinessException {
        if (!futureMap.containsKey(key)) {
            throw new BusinessException(StringUtils.format("实车回放任务{}不存在", key));
        }
        futureMap.get(key).suspend();
    }

    public static void goOn(String key) throws BusinessException {
        if (!futureMap.containsKey(key)) {
            return;
        }
        futureMap.get(key).goOn();
    }

    public static void stopSendingData(String key) throws BusinessException, IOException {
        if (!futureMap.containsKey(key)) {
            return;
        }
        futureMap.get(key).stopSendingData();
        futureMap.remove(key);
        log.info("删除回放任务{}", key);
    }
}
