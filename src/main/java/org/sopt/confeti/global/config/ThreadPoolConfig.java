package org.sopt.confeti.global.config;

import io.awspring.cloud.sqs.MessageExecutionThreadFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
public class ThreadPoolConfig {

    public static final String SQS_WORKER_PREFIX = "sqs-worker-";
    public static final String SQS_WORKER_PROVIDER_PREFIX = SQS_WORKER_PREFIX + "provider-";
    public static final String SQS_WORKER_CONSUMER_PREFIX = SQS_WORKER_PREFIX + "consumer-";

    public static final String MESSAGE_PROVIDER_POOL = "providerThreadPool";
    public static final String MESSAGE_CONSUMER_POOL = "consumerThreadPool";

    @Bean(MESSAGE_PROVIDER_POOL)
    public ThreadPoolTaskExecutor messageProvider() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();

        MessageExecutionThreadFactory threadFactory = new MessageExecutionThreadFactory();
        threadFactory.setThreadNamePrefix(SQS_WORKER_PROVIDER_PREFIX);
        executor.setThreadFactory(threadFactory);

        executor.setCorePoolSize(20);
        executor.setMaxPoolSize(40);
        executor.setQueueCapacity(50);
        executor.initialize();
        return executor;
    }

    @Bean(MESSAGE_CONSUMER_POOL)
    public ThreadPoolTaskExecutor messageConsumer() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();

        MessageExecutionThreadFactory threadFactory = new MessageExecutionThreadFactory();
        threadFactory.setThreadNamePrefix(SQS_WORKER_CONSUMER_PREFIX);
        executor.setThreadFactory(threadFactory);

        executor.setCorePoolSize(20);
        executor.setMaxPoolSize(40);
        executor.setQueueCapacity(50);
        executor.initialize();
        return executor;
    }

}
