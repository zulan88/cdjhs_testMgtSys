package net.wanji.business.schedule;

import net.wanji.business.domain.Tjshape;
import net.wanji.business.domain.bo.TrajectoryDetailBo;
import net.wanji.business.exception.BusinessException;
import net.wanji.business.socket.WebSocketManage;
import net.wanji.common.common.TrajectoryValueDto;
import net.wanji.common.utils.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Auther: guanyuduo
 * @Date: 2023/7/10 18:41
 * @Descriptoin:
 */

public class PlaybackSchedule {

    private static final Logger log = LoggerFactory.getLogger("business");

    static Map<String, PlaybackDomain> futureMap = new ConcurrentHashMap<>(16);

    static Map<String, PlaybackOnsite> onsiteMap = new ConcurrentHashMap<>(16);

    static Map<String, PreviewTask> previewMap = new ConcurrentHashMap<>(16);


    public static void startSendingData(String key, List<List<TrajectoryValueDto>> data) throws BusinessException, IOException {
        stopSendingData(key);
        futureMap.put(key, new PlaybackDomain(key, data));
        log.info("成功创建回放任务{}", key);
    }

    public static void startSendingOnsiteData(String key, List<Tjshape> data) throws BusinessException, IOException {
        stopSendingDataOnsite(key);
        onsiteMap.put(key, new PlaybackOnsite(key, data));
        log.info("成功创建回放任务{}", key);
    }

    public static void startPreview(String key, List<List<TrajectoryDetailBo>> data) throws BusinessException, IOException {
        stopPreview(key);
        previewMap.put(key, new PreviewTask(key, data));
        log.info("成功创建预览任务{}", key);
    }

    public static void suspend(String key) throws BusinessException {
        if (!futureMap.containsKey(key)) {
            throw new BusinessException(StringUtils.format("回放任务{}不存在", key));
        }
        futureMap.get(key).suspend();
        log.info("暂停回放任务{}", key);
    }

    public static void suspendOniste(String key) throws BusinessException {
        if (!onsiteMap.containsKey(key)) {
            throw new BusinessException(StringUtils.format("回放任务{}不存在", key));
        }
        onsiteMap.get(key).suspend();
        log.info("暂停回放任务{}", key);
    }

    public static void suspendPreview(String key) throws BusinessException {
        if (!previewMap.containsKey(key)) {
            throw new BusinessException(StringUtils.format("预览任务{}不存在", key));
        }
        previewMap.get(key).suspend();
        log.info("暂停预览任务{}", key);
    }

    public static void goOn(String key) throws BusinessException {
        if (!futureMap.containsKey(key)) {
            return;
        }
        futureMap.get(key).goOn();
        log.info("继续回放任务{}", key);
    }

    public static void goOnOnsite(String key) throws BusinessException {
        if (!onsiteMap.containsKey(key)) {
            return;
        }
        onsiteMap.get(key).goOn();
        log.info("继续回放任务{}", key);
    }

    public static void goOnPreview(String key) throws BusinessException {
        if (!previewMap.containsKey(key)) {
            return;
        }
        previewMap.get(key).goOn();
        log.info("继续预览任务{}", key);
    }

    public static void stopSendingData(String key) throws BusinessException, IOException {
        if (!futureMap.containsKey(key)) {
            return;
        }
        WebSocketManage.remove(key, true);
        futureMap.get(key).stopSendingData();
        futureMap.remove(key);
        log.info("删除回放任务{}", key);
    }

    public static void stopSendingDataOnsite(String key) throws BusinessException, IOException {
        if (!onsiteMap.containsKey(key)) {
            return;
        }
        WebSocketManage.remove(key, true);
        onsiteMap.get(key).stopSendingData();
        onsiteMap.remove(key);
        log.info("删除回放任务{}", key);
    }

    public static void stopPreview(String key) throws BusinessException, IOException {
        if (!previewMap.containsKey(key)) {
            return;
        }
        WebSocketManage.remove(key, true);
        previewMap.get(key).stopSendingData();
        previewMap.remove(key);
        log.info("删除预览任务{}", key);
    }

    public static void stopAll(String key) throws BusinessException, IOException {
        stopSendingData(key);
        stopSendingDataOnsite(key);
        stopPreview(key);
    }
}
