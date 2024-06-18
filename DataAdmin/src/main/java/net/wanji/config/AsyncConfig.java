package net.wanji.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import java.util.concurrent.Executor;

@Configuration
public class AsyncConfig implements AsyncConfigurer {

    @Override
    public Executor getAsyncExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(10); // 核心线程数
        executor.setMaxPoolSize(50);  // 最大线程数
        executor.setQueueCapacity(100); // 队列大小
        executor.setThreadNamePrefix("MyAsyncThread-"); // 线程名前缀
        executor.initialize(); // 初始化线程池
        return executor;
    }
}
