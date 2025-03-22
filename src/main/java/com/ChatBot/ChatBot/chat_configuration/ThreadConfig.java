package com.ChatBot.ChatBot.chat_configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.ThreadPoolExecutor;

@Configuration
@EnableAsync
public class ThreadConfig {
    @Bean(name = "myAsyncPoolTaskExecutor")
    public ThreadPoolTaskExecutor executor() {
        ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
        // Core thread count.
        taskExecutor.setCorePoolSize(10);
        // The maximum number of threads maintained in the thread pool. Only when the buffer queue is full will threads exceeding the core thread count be requested.
        taskExecutor.setMaxPoolSize(100);
        // Cache queue.
        taskExecutor.setQueueCapacity(50);
        // Allowed idle time. Threads other than core threads will be destroyed after the idle time arrives.
        taskExecutor.setKeepAliveSeconds(200);
        // Thread name prefix for asynchronous methods.
        taskExecutor.setThreadNamePrefix("async-");
        /**
         * When the task cache queue of the thread pool is full and the number of threads in the thread pool reaches maximumPoolSize, if there are still tasks coming, a task rejection policy will be adopted.
         * There are usually four policies:
         * ThreadPoolExecutor.AbortPolicy: Discard the task and throw RejectedExecutionException.
         * ThreadPoolExecutor.DiscardPolicy: Also discard the task, but do not throw an exception.
         * ThreadPoolExecutor.DiscardOldestPolicy: Discard the task at the front of the queue and then try to execute the task again (repeat this process).
         * ThreadPoolExecutor.CallerRunsPolicy: Retry adding the current task and automatically call the execute() method repeatedly until it succeeds.
         */
        taskExecutor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        taskExecutor.initialize();
        return taskExecutor;
    }
}
