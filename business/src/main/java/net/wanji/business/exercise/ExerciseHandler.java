package net.wanji.business.exercise;

import lombok.extern.slf4j.Slf4j;
import net.jodah.expiringmap.ExpirationPolicy;
import net.jodah.expiringmap.ExpiringMap;
import net.wanji.business.domain.CdjhsExerciseRecord;
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
import net.wanji.common.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.concurrent.*;

/**
 * @author: jenny
 * @create: 2024-06-22 6:01 下午
 */
@Slf4j
@Component
public class ExerciseHandler {
    public static ConcurrentHashMap<String, Integer> idleDeviceMap = new ConcurrentHashMap<>();

    public static ExpiringMap<String, Long> occupationMap = ExpiringMap.builder()
            .maxSize(5)
            .expirationPolicy(ExpirationPolicy.CREATED)
            .expiration(1, TimeUnit.DAYS)
            .build();

    public static LinkedBlockingQueue<CdjhsExerciseRecord> taskQueue = new LinkedBlockingQueue<>(50);

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

    //待配置到配置文件中
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


    @PostConstruct
    public void init(){
        Thread thread = new Thread(() -> {
            while (true){
                try {
                    if(!idleDeviceMap.isEmpty()){
                        //选择空闲且没有被占用的域控下发练习
                        String uniques = "";
                        for (String device : idleDeviceMap.keySet()) {
                            if (!occupationMap.containsKey(device)) {
                                uniques = device;
                                break;
                            }
                        }
                        if(StringUtils.isNotEmpty(uniques)){
                            CdjhsExerciseRecord record = null;
                            try {
                                record = taskQueue.take();
                                occupationMap.put(uniques, record.getId());//占用该域控

                                TaskExercise taskExercise = new TaskExercise(imageLengthThresold, record, uniques,
                                        tessIp, tessPort, radius, kafkaTopic, cdjhsExerciseRecordMapper, cdjhsDeviceImageRecordMapper,
                                        redisCache, imageListReportListener, imageDelResultListener, imageIssueResultListener, testIssueResultListener,
                                        restService, tjDeviceDetailMapper, redisMessageListenerContainer, kafkaProducer, dataFileService, kafkaTrajectoryConsumer, tjTaskMapper, interactionFuc);
                                executor.submit(taskExercise);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }catch (Exception e){
                    log.info("消费线程报错");
                    e.printStackTrace();
                }
            }
        });
        thread.start();
    }
}
