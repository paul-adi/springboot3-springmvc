package com.zxcv.springboot3_springmvc.config;

import io.micrometer.context.ContextExecutorService;
import io.micrometer.context.ContextSnapshotFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

@EnableAsync
@Configuration
@RequiredArgsConstructor
public class AsyncConfig implements AsyncConfigurer {

    private final ThreadPoolTaskExecutor taskExecutor;

    // continue Trace-Id and Span-Id on new thread
    @Override
    public Executor getAsyncExecutor() {
        return ContextExecutorService.wrap(taskExecutor.getThreadPoolExecutor(),
                ContextSnapshotFactory.builder().build()::captureAll);
    }

}
