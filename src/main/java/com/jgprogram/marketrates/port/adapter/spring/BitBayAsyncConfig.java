package com.jgprogram.marketrates.port.adapter.spring;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

@Configuration
public class BitBayAsyncConfig {

    private static final int NUMBER_OF_QUERIES_PER_REQUEST = 50;

    @Bean(name = "bibayThreadPool")
    public Executor getBitBayExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setMaxPoolSize(NUMBER_OF_QUERIES_PER_REQUEST);
        executor.setKeepAliveSeconds(4 * 60);
        executor.setThreadNamePrefix("BitBay-");
        executor.setQueueCapacity(100);
        executor.initialize();

        return executor;
    }
}
