package org.sopt.confeti.global.messagebroker.sqs.strategy;

import static org.sopt.confeti.global.message.ErrorMessage.INTERNAL_SERVER_ERROR;

import io.awspring.cloud.sqs.listener.SqsHeaders;
import io.awspring.cloud.sqs.operations.SqsTemplate;
import java.time.Duration;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.sopt.confeti.global.exception.ConfetiException;
import org.sopt.confeti.global.messagebroker.MessageBrokerStrategy;
import org.sopt.confeti.global.messagebroker.handler.EventHandler;
import org.sopt.confeti.global.messagebroker.message.Event;
import org.sopt.confeti.global.notification.NotificationAgent;
import org.sopt.confeti.global.notification.SlackNotificationType;
import org.sopt.confeti.global.util.JsonMapper;
import org.springframework.core.task.TaskExecutor;
import org.springframework.messaging.Message;
import org.springframework.scheduling.annotation.Scheduled;
import software.amazon.awssdk.services.sqs.SqsAsyncClient;
import software.amazon.awssdk.services.sqs.model.DeleteMessageRequest;
import software.amazon.awssdk.services.sqs.model.MessageAttributeValue;

@Slf4j
@Getter
public abstract class SqsStrategy extends MessageBrokerStrategy {

    private final String queueUrl;

    private final SqsTemplate sqsTemplate;
    private final NotificationAgent notificationAgent;
    private final TaskExecutor messageConsumeExecutor;
    private final SqsAsyncClient sqsAsyncClient;
    private final JsonMapper jsonMapper;

    private static final String ATTRIBUTE_DATA_TYPE_STRING = "string";
    private static final String ATTRIBUTE_EVENT_TYPE = "event";
    private static final String ATTRIBUTE_TRACE_ID = "traceId";
    private static final String ATTRIBUTE_TYPE_ID = "confetiEventType";

    protected SqsStrategy(
        List<? extends EventHandler<? extends Event>> eventHandlers,
        String queueUrl,
        SqsTemplate sqsTemplate,
        NotificationAgent notificationAgent,
        TaskExecutor messageConsumeExecutor,
        SqsAsyncClient sqsAsyncClient,
        JsonMapper jsonMapper
    ) {
        super(eventHandlers);
        this.queueUrl = queueUrl;
        this.sqsTemplate = sqsTemplate;
        this.notificationAgent = notificationAgent;
        this.messageConsumeExecutor = messageConsumeExecutor;
        this.sqsAsyncClient = sqsAsyncClient;
        this.jsonMapper = jsonMapper;
    }

    protected void asyncSend(String queueName, Event event) {
        Map<String, Object> messageAttributes = createMessageAttributes(event);
        String serializedData = jsonMapper.toJson(event);

        sqsTemplate.sendAsync(to -> to
                .queue(queueName)
                .payload(serializedData)
                .headers(messageAttributes)
            )
            .exceptionally(exception -> {
                String errorMessage = extractExceptionMessage(exception, event);
                notificationAgent.notify(SlackNotificationType.HIGH_ERROR, errorMessage);
                log.error("[SqsService send exception] {}", errorMessage, exception);
                return null;
            });
    }

    private Map<String, Object> createMessageAttributes(Event event) {
        String eventType = event.getClass().getSimpleName();
        String eventTraceId = UUID.randomUUID().toString();

        return Map.of(
            ATTRIBUTE_EVENT_TYPE, MessageAttributeValue.builder()
                .stringValue(eventType)
                .dataType(ATTRIBUTE_DATA_TYPE_STRING)
                .build(),
            ATTRIBUTE_TRACE_ID, MessageAttributeValue.builder()
                .stringValue(eventTraceId)
                .dataType(ATTRIBUTE_DATA_TYPE_STRING)
                .build(),
            ATTRIBUTE_TYPE_ID, getEventTypeId(event)
        );
    }

    private String getEventTypeId(Event event) {
        return getEventHandlers()
            .values()
            .stream()
            .filter(eventHandler -> eventHandler.isSupported(event.getClass()))
            .findFirst()
            .orElseThrow(() -> new ConfetiException(INTERNAL_SERVER_ERROR))
            .getSupportedTypeId();
    }

    private <T> String extractExceptionMessage(Throwable exception, T data) {
        return String.format("[SQS Exception] Exception Message: %s, Data: %s",
            exception.getMessage(),
            data.toString());
    }

    protected CompletableFuture<Void> processMessage(Collection<Message<?>> messages) {
        List<CompletableFuture<Void>> futures = messages.stream()
            .map(message -> {
                return CompletableFuture.runAsync(() -> {
                        String type = message.getHeaders().get(ATTRIBUTE_TYPE_ID, String.class);
                        String payload = message.getPayload().toString();
                        handleEvent(type, payload);
                    }, messageConsumeExecutor)
                    .exceptionally(e -> {
                        log.error("message: {}", message.toString(), e);
                        return null;
                    });
            })
            .toList();

        return CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]));
    }

    protected void handleEvent(String type, String payload) {
        EventHandler<? extends Event> eventHandler = super.getEventHandlers().get(type);
        if (eventHandler == null) {
            log.error("Cannot find event handler: {}. Payload: {}", type, payload);
            throw new ConfetiException(INTERNAL_SERVER_ERROR);
        }

        processEvent(eventHandler, payload);
    }

    protected <T extends Event> void processEvent(EventHandler<T> eventHandler, String payload) {
        Class<T> supportedEventType = eventHandler.getSupportedType();
        T event = jsonMapper.fromJson(supportedEventType, payload);

        eventHandler.handle(event);
    }

    @Scheduled(fixedDelay = 1000)
    public void pollSqsMessages() {

        Collection<Message<?>> receivedMessages = sqsTemplate.receiveMany(options -> options
            .queue(queueUrl)
            .maxNumberOfMessages(10)
            .pollTimeout(Duration.ofSeconds(20))
        );

        if (receivedMessages.isEmpty()) {
            return;
        }

        processMessage(receivedMessages)
            .thenRun(() -> {
                receivedMessages.forEach(this::deleteMessage);
            })
            .exceptionally(e -> {
                log.error("[SQS Polling Exception] message: {}", receivedMessages, e);
                return null;
            });
    }

    private void deleteMessage(Message<?> message) {
        String receiptHandle = (String) message.getHeaders()
            .get(SqsHeaders.SQS_RECEIPT_HANDLE_HEADER);

        DeleteMessageRequest deleteMessageRequest = DeleteMessageRequest.builder()
            .queueUrl(queueUrl)
            .receiptHandle(receiptHandle)
            .build();
        sqsAsyncClient.deleteMessage(deleteMessageRequest);
    }

}
