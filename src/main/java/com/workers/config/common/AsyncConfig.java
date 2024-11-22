package com.workers.config.common;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

@EnableAsync
@Configuration
public class AsyncConfig {

    @Value("${order-service.executor.core-pool-size}")
    public int corePoolSize;
    @Value("${order-service.executor.max-pool-size:20}") // Задаем максимальный размер пула
    private int maxPoolSize;
    @Value("${order-service.executor.queue-capacity:100}") // Размер очереди задач
    private int queueCapacity;

    @Bean(name = "order_service_executor")
    public Executor legacyPayrollExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(corePoolSize);
        executor.setMaxPoolSize(maxPoolSize);
        executor.setQueueCapacity(queueCapacity);
        executor.setThreadNamePrefix("order-service-exec");
        executor.initialize();
        return executor;
    }


}
