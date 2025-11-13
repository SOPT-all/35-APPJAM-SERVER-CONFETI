package org.sopt.confeti.global.config;

import static org.sopt.confeti.global.config.ThreadPoolConfig.MESSAGE_CONSUMER_POOL;
import static org.sopt.confeti.global.config.ThreadPoolConfig.MESSAGE_PROVIDER_POOL;

import io.awspring.cloud.sqs.config.SqsMessageListenerContainerFactory;
import io.awspring.cloud.sqs.listener.acknowledgement.handler.AcknowledgementMode;
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

    /**
     * AcknowledgementMode.MANUAL: 폴링해온 메세지 목록의 삭제를 개별적으로 수행하기 위함 autoStartup(false): start 시점을
     * 애플리케이션 실행 이후에 수동으로 지정
     */
    @Bean
    public SqsMessageListenerContainerFactory createEventSqsListenerContainerFactory(
        SqsAsyncClient sqsAsyncClient,
        @Qualifier(MESSAGE_CONSUMER_POOL) TaskExecutor consumerExecutor
    ) {
        return SqsMessageListenerContainerFactory
            .builder()
            .configure(options -> options
                .componentsTaskExecutor(consumerExecutor)
                .acknowledgementMode(AcknowledgementMode.MANUAL)
                .maxMessagesPerPoll(10)
                .pollTimeout(Duration.ofSeconds(20))
                .autoStartup(false))
            .sqsAsyncClient(sqsAsyncClient)
            .build();
    }

}
