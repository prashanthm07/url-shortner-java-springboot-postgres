package com.prashanth.url_shortner.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

@Configuration
@EnableAsync
public class AsyncConfig {

    @Bean(name = "taskExecutor")
    public Executor taskExecutor(){
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(2); // Minumum number of threads
        executor.setMaxPoolSize(5); // Maximum number of threads
        executor.setQueueCapacity(100); // Queue capacity
        executor.setThreadNamePrefix("Async-");
        executor.initialize();

        return executor;
    }
}
