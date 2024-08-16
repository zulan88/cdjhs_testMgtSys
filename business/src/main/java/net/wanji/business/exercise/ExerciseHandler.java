package net.wanji.business.exercise;

import lombok.extern.slf4j.Slf4j;
import net.jodah.expiringmap.ExpirationPolicy;
import net.jodah.expiringmap.ExpiringMap;
import net.wanji.business.domain.CdjhsExerciseRecord;
import net.wanji.business.domain.tess.ParamConfig;
import net.wanji.business.exercise.enums.TaskStatusEnum;
import net.wanji.business.listener.ImageDelResultListener;
import net.wanji.business.listener.ImageIssueResultListener;
import net.wanji.business.listener.ImageListReportListener;
import net.wanji.business.listener.TestIssueResultListener;
import net.wanji.business.mapper.CdjhsDeviceImageRecordMapper;
import net.wanji.business.mapper.CdjhsExerciseRecordMapper;
import net.wanji.business.mapper.TjDeviceDetailMapper;
import net.wanji.business.mapper.TjTaskMapper;
import net.wanji.business.service.KafkaProducer;
import net.wanji.business.service.RestService;
import net.wanji.business.service.record.DataFileService;
import net.wanji.business.trajectory.KafkaTrajectoryConsumer;
import net.wanji.business.util.InteractionFuc;
import net.wanji.common.core.redis.RedisCache;
import net.wanji.common.utils.RedisKeyUtils;
import net.wanji.common.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

/**
 * @author: jenny
 * @create: 2024-06-22 6:01 下午
 */
@Slf4j
@Component
public class ExerciseHandler {
    public static ConcurrentHashMap<Long, Future<?>> taskThreadMap = new ConcurrentHashMap<>();

    public static ExpiringMap<String, Long> occupationMap = ExpiringMap.builder()
            .maxSize(30)
            .expirationPolicy(ExpirationPolicy.CREATED)
            .expiration(3, TimeUnit.HOURS)
            .build();

    public static LinkedBlockingQueue<CdjhsExerciseRecord> taskQueue = new LinkedBlockingQueue<>(100);

    public static ReentrantLock lock = new ReentrantLock();

    private static ThreadPoolExecutor executor = new ThreadPoolExecutor(15, 30,
            10, TimeUnit.SECONDS,
            new ArrayBlockingQueue<>(50), new RejectedExecutionHandler() {
        @Override
        public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
            try {
                executor.getQueue().put(r);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    });

    @Autowired
    private CdjhsExerciseRecordMapper cdjhsExerciseRecordMapper;

    @Autowired
    private CdjhsDeviceImageRecordMapper cdjhsDeviceImageRecordMapper;

    @Autowired
    private RedisCache redisCache;

    @Autowired
    private ImageListReportListener imageListReportListener;

    @Autowired
    private ImageDelResultListener imageDelResultListener;

    @Autowired
    private ImageIssueResultListener imageIssueResultListener;

    @Autowired
    private TestIssueResultListener testIssueResultListener;

    @Autowired
    private RestService restService;

    @Autowired
    private TjDeviceDetailMapper tjDeviceDetailMapper;

    @Autowired
    private RedisMessageListenerContainer redisMessageListenerContainer;

    @Autowired
    private KafkaProducer kafkaProducer;

    @Autowired
    private DataFileService dataFileService;

    @Autowired
    private KafkaTrajectoryConsumer kafkaTrajectoryConsumer;

    @Autowired
    private TjTaskMapper tjTaskMapper;

    @Autowired
    private InteractionFuc interactionFuc;

    @Autowired
    private TimeoutConfig timeoutConfig;

    @Autowired
    private ParamConfig paramConfig;

    @Autowired
    private BindingConfig bindingConfig;

    public static ConcurrentHashMap<String, LinkedBlockingQueue<CdjhsExerciseRecord>> bindedTaskQueue = new ConcurrentHashMap<>();

    public static ConcurrentHashMap<String, ReentrantLock> binedLockMap = new ConcurrentHashMap<>();


    @PostConstruct
    public void init() {
        boolean enabled = bindingConfig.getEnabled();
        Map<String, String> relationship = bindingConfig.getRelationship();
        //非绑定消费线程
        Thread thread = new Thread(() -> {
            while (true) {
                //选择在线且没有被占用的域控下发练习
                List<String> devices = getOnlineAndIdleDevice(enabled, relationship);
                if(!devices.isEmpty() && !taskQueue.isEmpty()){
                    String uniques = devices.get(0);
                    lock.lock();
                    try {
                        CdjhsExerciseRecord record = taskQueue.poll();
                        if(null != record){
                            run(record, uniques);
                            //更新任务前方排队人数
                            CdjhsExerciseRecord[] queueArray = taskQueue.toArray(new CdjhsExerciseRecord[0]);
                            updateWaitingNum(queueArray);
                        }

                    }catch (Exception e){
                        log.error("消费线程报错");
                        e.printStackTrace();
                    }finally {
                        lock.unlock();
                    }
                }
            }
        });
        thread.start();

        //绑定消费线程
        if(enabled && !relationship.isEmpty()){
            Set<String> set = new HashSet<>(relationship.values());
            for(String device: set){
                bindedTaskQueue.putIfAbsent(device, new LinkedBlockingQueue<>(30));
                binedLockMap.putIfAbsent(device, new ReentrantLock());
            }

            Thread bindedThread = new Thread(() -> {
                while (true){
                    for(Map.Entry<String, LinkedBlockingQueue<CdjhsExerciseRecord>> entry: bindedTaskQueue.entrySet()){
                        String device = entry.getKey();
                        LinkedBlockingQueue<CdjhsExerciseRecord> queue = entry.getValue();
                        boolean isIdled = isIdle(device);
                        if(isIdled && !queue.isEmpty()){
                            ReentrantLock lock = binedLockMap.get(device);
                            lock.lock();
                            try {
                                CdjhsExerciseRecord record = queue.poll();
                                if(null != record){
                                    run(record, device);
                                    //更新队列中任务前方排队人数
                                    CdjhsExerciseRecord[] queueArray = queue.toArray(new CdjhsExerciseRecord[0]);
                                    updateWaitingNum(queueArray);
                                }
                            }catch (Exception e){
                                log.error("绑定域控{}消费线程报错", device);
                                e.printStackTrace();
                            }finally {
                                lock.unlock();
                            }
                        }
                    }
                }
            });
            bindedThread.start();
        }
    }

    private boolean isIdle(String device) {
        String key = RedisKeyUtils.getDeviceStatusKey(device);
        Integer state = redisCache.getCacheObject(key);
        return Objects.nonNull(state) && state == 2 && !occupationMap.containsKey(device);
    }

    public void run(CdjhsExerciseRecord record, String uniques) {
        occupationMap.put(uniques, record.getId());//占用该域控
        TaskExercise taskExercise = new TaskExercise(record, uniques,
                cdjhsExerciseRecordMapper, cdjhsDeviceImageRecordMapper,
                redisCache, imageListReportListener, imageDelResultListener,
                imageIssueResultListener, testIssueResultListener,
                restService, tjDeviceDetailMapper, redisMessageListenerContainer,
                kafkaProducer, dataFileService, kafkaTrajectoryConsumer,
                tjTaskMapper, interactionFuc, timeoutConfig, paramConfig);
        Future<?> future = executor.submit(taskExercise);
        taskThreadMap.put(record.getId(), future);
    }

    private void updateWaitingNum(CdjhsExerciseRecord[] queueArray) {
        if(queueArray.length > 0){
            for (int i = 0; i < queueArray.length; i++) {
                queueArray[i].setWaitingNum(i);
                queueArray[i].setStatus(TaskStatusEnum.WAITING.getStatus());
            }
            cdjhsExerciseRecordMapper.updateBatch(Arrays.asList(queueArray));
        }
    }

    public static boolean forceEndTask(Long taskId){
        if(!taskThreadMap.containsKey(taskId)){
            log.info("练习任务{}不存在", taskId);
        }else{
            Future<?> future = taskThreadMap.get(taskId);
            if(future != null && !(future.isCancelled() || future.isDone())){
                return future.cancel(true);
            }
        }
        return false;
    }

    private List<String> getOnlineAndIdleDevice() {
        List<String> list = new ArrayList<>();
        String prefix = RedisKeyUtils.DEVICE_STATUS_PRE + RedisKeyUtils.DEVICE_STATUS_PRE_LINK;
        Set<String> keys = redisCache.getKeys(prefix);
        if(StringUtils.isNotEmpty(keys)){
            for(String key: keys){
                Integer state = redisCache.getCacheObject(key);
                if(Objects.nonNull(state)){
                    String[] split = key.split(RedisKeyUtils.DEVICE_STATUS_PRE_LINK);
                    String uniques = split[1];
                    if(state == 2){
                        list.add(uniques);
                    }
                }
            }
            list = list.stream()
                    .filter(device -> !occupationMap.containsKey(device))
                    .collect(Collectors.toList());
        }
        return list;
    }

    private List<String> getOnlineAndIdleDevice(boolean enabled, Map<String, String> relationship){
        List<String> list = getOnlineAndIdleDevice();
        if(enabled && !relationship.isEmpty()){
            Set<String> set = new HashSet<>(relationship.values());
            list = list.stream()
                    .filter(device -> !set.contains(device))
                    .collect(Collectors.toList());
        }
        return list;
    }
}
