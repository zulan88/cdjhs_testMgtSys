package net.wanji.business.exercise;

import lombok.extern.slf4j.Slf4j;
import net.jodah.expiringmap.ExpirationPolicy;
import net.jodah.expiringmap.ExpiringMap;
import net.wanji.business.domain.CdjhsExerciseRecord;
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

/**
 * @author: jenny
 * @create: 2024-06-22 6:01 下午
 */
@Slf4j
@Component
public class ExerciseHandler {
    public static ConcurrentHashMap<Long, TaskExerciseDto> taskThreadMap = new ConcurrentHashMap<>();

    public static ExpiringMap<String, Long> occupationMap = ExpiringMap.builder()
            .maxSize(5)
            .expirationPolicy(ExpirationPolicy.CREATED)
            .expiration(3, TimeUnit.HOURS)
            .build();

    public static LinkedBlockingQueue<CdjhsExerciseRecord> taskQueue = new LinkedBlockingQueue<>(100);

    public static LinkedBlockingQueue<CdjhsExerciseRecord> tempTaskQueue = new LinkedBlockingQueue<>(5);

    public static ReentrantLock lock = new ReentrantLock();

    public static ReentrantLock tempLock = new ReentrantLock();

    private static ThreadPoolExecutor executor = new ThreadPoolExecutor(3, 5,
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

    @Value("${image.length.thresold}")
    private Integer imageLengthThresold;

    @Value("${tess.ip}")
    private String tessIp;

    @Value("${tess.port}")
    private Integer tessPort;

    @Value("${trajectory.radius}")
    private Double radius;

    @Value("${trajectory.topic}")
    private String kafkaTopic;

    @Value("${spring.kafka.bootstrap-servers}")
    private String kafkaHost;


    @PostConstruct
    public void init(){
        Thread thread = new Thread(() -> {
            while (true){
                //选择在线且没有被占用的域控下发练习
                List<String> devices = getOnlineAndIdleDevice();
                if(!devices.isEmpty() && !taskQueue.isEmpty()){
                    String uniques = "";
                    for(String device: devices){
                        if(!occupationMap.containsKey(device) && !device.equals("YK001")){
                            uniques = device;
                            break;
                        }
                    }
                    if(StringUtils.isNotEmpty(uniques)){
                        lock.lock();
                        try {
                            CdjhsExerciseRecord record = taskQueue.poll();
                            if(null != record){
                                occupationMap.put(uniques, record.getId());//占用该域控
                                TaskExercise taskExercise = new TaskExercise(imageLengthThresold, record, uniques,
                                        tessIp, tessPort, radius, kafkaTopic, kafkaHost, cdjhsExerciseRecordMapper, cdjhsDeviceImageRecordMapper,
                                        redisCache, imageListReportListener, imageDelResultListener, imageIssueResultListener, testIssueResultListener,
                                        restService, tjDeviceDetailMapper, redisMessageListenerContainer, kafkaProducer, dataFileService, kafkaTrajectoryConsumer,
                                        tjTaskMapper, interactionFuc, timeoutConfig);
                                Future<?> future = executor.submit(taskExercise);
                                TaskExerciseDto taskExerciseDto = new TaskExerciseDto(taskExercise, future);
                                taskThreadMap.put(record.getId(), taskExerciseDto);

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
            }
        });
        thread.start();


        Thread tempConsumeThread = new Thread(() -> {
            while (true) {
                List<String> devices = getOnlineAndIdleDevice();
                if(!devices.isEmpty() && !tempTaskQueue.isEmpty()){
                    String uniques = "";
                    for(String device: devices){
                        if(!occupationMap.containsKey(device) && device.equals("YK001")){
                            uniques = device;
                            break;
                        }
                    }
                    if(StringUtils.isNotEmpty(uniques)){
                        tempLock.lock();
                        try {
                            CdjhsExerciseRecord record = tempTaskQueue.poll();
                            if(null != record){
                                occupationMap.put(uniques, record.getId());//占用该域控

                                TaskExercise taskExercise = new TaskExercise(imageLengthThresold, record, uniques,
                                        tessIp, tessPort, radius, kafkaTopic, kafkaHost, cdjhsExerciseRecordMapper, cdjhsDeviceImageRecordMapper,
                                        redisCache, imageListReportListener, imageDelResultListener, imageIssueResultListener, testIssueResultListener,
                                        restService, tjDeviceDetailMapper, redisMessageListenerContainer, kafkaProducer, dataFileService, kafkaTrajectoryConsumer,
                                        tjTaskMapper, interactionFuc, timeoutConfig);
                                Future<?> future = executor.submit(taskExercise);
                                TaskExerciseDto taskExerciseDto = new TaskExerciseDto(taskExercise, future);
                                taskThreadMap.put(record.getId(), taskExerciseDto);

                                //更新任务前方排队人数
                                CdjhsExerciseRecord[] queueArray = taskQueue.toArray(new CdjhsExerciseRecord[0]);
                                updateWaitingNum(queueArray);
                            }
                        }catch (Exception e){
                            log.error("临时消费线程报错");
                            e.printStackTrace();
                        }finally {
                            tempLock.unlock();
                        }
                    }
                }
            }
        });
        tempConsumeThread.start();
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
            Future<?> future = taskThreadMap.get(taskId).getFuture();
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
        }
        return list;
    }
}
