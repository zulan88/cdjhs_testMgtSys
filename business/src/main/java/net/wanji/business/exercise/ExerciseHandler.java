package net.wanji.business.exercise;

import net.jodah.expiringmap.ExpirationPolicy;
import net.jodah.expiringmap.ExpiringMap;
import net.wanji.business.domain.CdjhsExerciseRecord;
import net.wanji.business.mapper.CdjhsExerciseRecordMapper;
import net.wanji.common.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.concurrent.*;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author: jenny
 * @create: 2024-06-22 6:01 下午
 */
@Component
public class ExerciseHandler {
    public static ExpiringMap<String, Integer> idleDeviceMap = ExpiringMap.builder()
            .maxSize(5)
            .expirationPolicy(ExpirationPolicy.CREATED)
            .expiration(2, TimeUnit.SECONDS)
            .build();

    public static ExpiringMap<String, Long> occupationMap = ExpiringMap.builder()
            .maxSize(5)
            .expirationPolicy(ExpirationPolicy.CREATED)
            .expiration(1, TimeUnit.DAYS)
            .build();

    private static LinkedBlockingQueue<CdjhsExerciseRecord> taskQueue = new LinkedBlockingQueue<>(50);

    private static ReentrantLock lock = new ReentrantLock();

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

    //待配置到配置文件中
    @Value("${image.length.thresold}")
    private Integer imageLengthThresold;

    @Value("${tess.ip}")
    private String tessIp;

    @Value("${tess.port}")
    private Integer tessPort;


    @PostConstruct
    public void init(){
        Thread thread = new Thread(() -> {
            while (true){
                if(!idleDeviceMap.isEmpty()){
                    //选择空闲且没有被占用的域控下发练习
                    String uniques = "";
                    for(String device: idleDeviceMap.keySet()){
                        if(!occupationMap.containsKey(device)){
                            uniques = device;
                            break;
                        }
                    }
                    if(StringUtils.isNotEmpty(uniques) && taskQueue.size() > 0){
                        CdjhsExerciseRecord record = null;
                        try {
                            record = taskQueue.take();
                            occupationMap.put(uniques, record.getId());//占用该域控

                            TaskExercise taskExercise = new TaskExercise(imageLengthThresold, record, uniques, tessIp, tessPort);
                            executor.submit(taskExercise);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        });
        thread.start();
    }

    public void putIntoTaskQueue(CdjhsExerciseRecord record){
        lock.lock();
        try {
            int size = taskQueue.size();
            //当没有空闲域控时，将该任务放入任务队列等待执行
            taskQueue.put(record);
            record.setStatus(1);
            record.setWaitingNum(size);
            cdjhsExerciseRecordMapper.updateCdjhsExerciseRecord(record);
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            lock.unlock();
        }
    }
}
