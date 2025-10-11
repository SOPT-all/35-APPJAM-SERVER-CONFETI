package org.sopt.confeti.global.messagebroker.sqs.strategy;

import static org.sopt.confeti.global.config.ThreadPoolConfig.MESSAGE_CONSUMER_POOL;

import io.awspring.cloud.sqs.annotation.SqsListener;
import io.awspring.cloud.sqs.operations.SqsTemplate;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import lombok.extern.slf4j.Slf4j;
import org.sopt.confeti.global.messagebroker.handler.CreateEventHandler;
import org.sopt.confeti.global.messagebroker.message.CreateEvent;
import org.sopt.confeti.global.messagebroker.message.Event;
import org.sopt.confeti.global.messagebroker.sqs.handler.SqsEventHandler;
import org.sopt.confeti.global.notification.NotificationAgent;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.task.TaskExecutor;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class CreateSqsStrategy<T extends CreateEventHandler<? extends CreateEvent> & SqsEventHandler>
    extends SqsStrategy {

    protected CreateSqsStrategy(
        List<T> createEventHandlers,
        @Value("${event.queues.confeti-server}") String queueName,
        SqsTemplate sqsTemplate,
        NotificationAgent notificationAgent,
        @Qualifier(MESSAGE_CONSUMER_POOL) TaskExecutor messageConsumeExecutor
    ) {
        super(createEventHandlers, queueName, sqsTemplate, notificationAgent,
            messageConsumeExecutor);
    }

    @Override
    public void sendEvent(Event event) {
        asyncSend(super.getQueueName(), event);
    }

    @Override
    @SqsListener(value = "${event.queues.confeti-server}")
    protected CompletableFuture<Void> listen(List<Message<String>> messages) {
        return super.processMessage(messages);
    }

}
