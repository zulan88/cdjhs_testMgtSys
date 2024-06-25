package net.wanji.business.oss;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author: jenny
 * @create: 2024-06-21 10:16 上午
 */
@Configuration
@Data
public class OssConfig {
    @Value("${aliyun.oss.endPoint}")
    private String endPoint;

    @Value("${aliyun.oss.accessKeyId}")
    private String accessKeyId;

    @Value("${aliyun.oss.accessKeySecret}")
    private String accessKeySecret;

    @Value("${aliyun.oss.bucketName}")
    private String bucketName;

    @Bean(name="fileDownloadHandlePool")
    public ThreadPoolTaskExecutor fileDownloadHandlePool(){
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        //表示线程池核心线程，正常情况下开启的线程数量
        executor.setCorePoolSize(5);
        //当核心线程都在跑任务，还有多余的任务会存在此处
        executor.setMaxPoolSize(10);
        //配置队列大小
        executor.setQueueCapacity(50);
        //非核心线程的超时时长，超长后会被回收
        executor.setKeepAliveSeconds(10);
        //配置线程池前缀
        executor.setThreadNamePrefix("FileDownloadHandlePool-");
        //用来设置线程池关闭的时候等待所有任务都完成再继续销毁其他的Bean
        executor.setWaitForTasksToCompleteOnShutdown(true);
        // 任务的等待时间 如果超过这个时间还没有销毁就 强制销毁，以确保应用最后能够被关闭，而不是阻塞住。
        executor.setAwaitTerminationSeconds(60);
        //配置线程池拒绝策略
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        //初始化线程池
        executor.initialize();

        return executor;
    }
}
