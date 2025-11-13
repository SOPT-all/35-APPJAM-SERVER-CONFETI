package org.sopt.confeti.global.messagebroker.sqs.strategy;

import static org.sopt.confeti.global.config.ThreadPoolConfig.MESSAGE_CONSUMER_POOL;

import io.awspring.cloud.sqs.annotation.SqsListener;
import io.awspring.cloud.sqs.operations.SqsTemplate;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.sopt.confeti.global.messagebroker.handler.CreateEventHandler;
import org.sopt.confeti.global.messagebroker.message.CreateEvent;
import org.sopt.confeti.global.messagebroker.message.Event;
import org.sopt.confeti.global.messagebroker.sqs.handler.SqsEventHandler;
import org.sopt.confeti.global.notification.NotificationAgent;
import org.sopt.confeti.global.util.JsonMapper;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.task.TaskExecutor;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.services.sqs.SqsAsyncClient;

@Slf4j
@Component
public class CreateSqsStrategy<T extends CreateEventHandler<? extends CreateEvent> & SqsEventHandler>
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
    public void sendEvent(Event event) {
        asyncSend(super.getQueueUrl(), event);
    }

    @Override
    @SqsListener(value = "${message-broker.sqs.create-event}", factory = "createEventSqsListenerContainerFactory")
    protected void listen(List<Message<String>> messages) {
        pollSqsMessages(messages);
    }
}
