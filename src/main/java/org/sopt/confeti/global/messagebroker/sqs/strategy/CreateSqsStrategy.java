package org.sopt.confeti.global.messagebroker.sqs.strategy;

import static org.sopt.confeti.global.config.ThreadPoolConfig.MESSAGE_CONSUMER_POOL;

import io.awspring.cloud.sqs.annotation.SqsListener;
import io.awspring.cloud.sqs.operations.SqsTemplate;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.sopt.confeti.global.messagebroker.handler.CreateMessageHandler;
import org.sopt.confeti.global.messagebroker.message.CreateMessage;
import org.sopt.confeti.global.messagebroker.message.Message;
import org.sopt.confeti.global.messagebroker.sqs.handler.SqsMessageHandler;
import org.sopt.confeti.global.notification.NotificationAgent;
import org.sopt.confeti.global.util.JsonMapper;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.services.sqs.SqsAsyncClient;

@Slf4j
@Component
public class CreateSqsStrategy<T extends CreateMessageHandler<? extends CreateMessage> & SqsMessageHandler>
    extends SqsStrategy {

    protected CreateSqsStrategy(
        List<T> createEventHandlers,
        @Value("${message-broker.sqs.create-event}") String queueUrl,
        SqsTemplate sqsTemplate,
        NotificationAgent notificationAgent,
        @Qualifier(MESSAGE_CONSUMER_POOL) TaskExecutor messageConsumeExecutor,
        SqsAsyncClient sqsAsyncClient,
        JsonMapper jsonMapper
    ) {
        super(createEventHandlers, queueUrl, sqsTemplate, notificationAgent,
            messageConsumeExecutor, sqsAsyncClient, jsonMapper);
    }

    @Override
    public void publish(Message message) {
        asyncSend(super.getQueueUrl(), message);
    }

    @Override
    @SqsListener(value = "${message-broker.sqs.create-event}", factory = "createEventSqsListenerContainerFactory")
    protected void listen(List<org.springframework.messaging.Message<String>> messages) {
        consumeSqsMessages(messages);
    }
}
