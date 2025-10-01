package org.sopt.confeti.global.config;

import static org.sopt.confeti.global.config.ThreadPoolConfig.MESSAGE_CONSUMER_POOL;
import static org.sopt.confeti.global.config.ThreadPoolConfig.MESSAGE_PROVIDER_POOL;

import io.awspring.cloud.sqs.config.SqsMessageListenerContainerFactory;
import io.awspring.cloud.sqs.operations.SqsTemplate;
import java.time.Duration;
import java.util.concurrent.Executor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import software.amazon.awssdk.core.client.config.SdkAdvancedAsyncClientOption;
import software.amazon.awssdk.services.sqs.SqsAsyncClient;

@Configuration
public class SqsConfig {

    @Bean
    public SqsAsyncClient sqsAsyncClient(
        @Qualifier(MESSAGE_PROVIDER_POOL) Executor providerExecutor
    ) {
        return SqsAsyncClient.builder()
            .asyncConfiguration(
                config -> config.advancedOption(
                    SdkAdvancedAsyncClientOption.FUTURE_COMPLETION_EXECUTOR, providerExecutor))
            .build();
    }

    @Bean
    public SqsTemplate sqsTemplate(SqsAsyncClient sqsAsyncClient) {
        return SqsTemplate.builder()
            .sqsAsyncClient(sqsAsyncClient)
            .build();
    }

    @Bean
    public SqsMessageListenerContainerFactory defaultSqsListenerContainerFactory(
        SqsAsyncClient sqsAsyncClient,
        @Qualifier(MESSAGE_CONSUMER_POOL) TaskExecutor consumerExecutor
    ) {
        return SqsMessageListenerContainerFactory
            .builder()
            .configure(options -> options
                .componentsTaskExecutor(consumerExecutor)
                .maxConcurrentMessages(30)
                .maxMessagesPerPoll(10)
                .pollTimeout(Duration.ofSeconds(20)))
            .sqsAsyncClient(sqsAsyncClient)
            .build();
    }

}
