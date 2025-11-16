package org.sopt.confeti.global.messagebroker.sqs.strategy;

import static org.sopt.confeti.global.message.ErrorMessage.INTERNAL_SERVER_ERROR;

import io.awspring.cloud.sqs.listener.SqsHeaders;
import io.awspring.cloud.sqs.operations.SqsTemplate;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.sopt.confeti.global.exception.ConfetiException;
import org.sopt.confeti.global.messagebroker.MessageBrokerStrategy;
import org.sopt.confeti.global.messagebroker.handler.MessageHandler;
import org.sopt.confeti.global.messagebroker.message.Message;
import org.sopt.confeti.global.notification.NotificationAgent;
import org.sopt.confeti.global.notification.SlackNotificationType;
import org.sopt.confeti.global.util.JsonMapper;
import org.springframework.core.task.TaskExecutor;
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
        List<? extends MessageHandler<? extends Message>> eventHandlers,
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

    abstract void listen(List<org.springframework.messaging.Message<String>> messages);

    protected void asyncSend(String queueName, Message message) {
        Map<String, Object> messageAttributes = createMessageAttributes(message);
        String serializedData = jsonMapper.toJson(message);

        sqsTemplate.sendAsync(to -> to
                .queue(queueName)
                .payload(serializedData)
                .headers(messageAttributes)
            )
            .exceptionally(exception -> {
                String errorMessage = extractExceptionMessage(exception, message);
                notificationAgent.notify(SlackNotificationType.HIGH_ERROR, errorMessage);
                log.error("[SqsService send exception] {}", errorMessage, exception);
                return null;
            });
    }

    private Map<String, Object> createMessageAttributes(Message message) {
        String eventType = message.getClass().getSimpleName();
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
            ATTRIBUTE_TYPE_ID, getEventTypeId(message)
        );
    }

    private String getEventTypeId(Message message) {
        MessageHandler<? extends Message> messageHandler = getHandlerByMessageClass(
            message.getClass());
        return messageHandler.getSupportedTypeId();
    }

    private <T> String extractExceptionMessage(Throwable exception, T data) {
        return String.format("[SQS Exception] Exception Message: %s, Data: %s",
            exception.getMessage(),
            data.toString());
    }

    protected CompletableFuture<Void> processMessageAndDelete(
        org.springframework.messaging.Message<?> message) {
        return CompletableFuture.runAsync(() -> {
                String type = message.getHeaders().get(ATTRIBUTE_TYPE_ID, String.class);
                String payload = message.getPayload().toString();
                handleEvent(type, payload);
            }, messageConsumeExecutor)
            .thenRun(() -> deleteMessage(message))
            .exceptionally(e -> {
                log.error("[Process Message Error]: {}", message.toString(), e);
                return null;
            });
    }

    protected void handleEvent(String typeId, String payload) {
        MessageHandler<? extends Message> messageHandler = getHandlerByTypeId(typeId);
        if (messageHandler == null) {
            log.error("Cannot find event handler: {}. Payload: {}", typeId, payload);
            throw new ConfetiException(INTERNAL_SERVER_ERROR);
        }

        processEvent(messageHandler, payload);
    }

    protected <T extends Message> void processEvent(MessageHandler<T> messageHandler,
        String payload) {
        Class<T> supportedEventType = messageHandler.getSupportedType();
        T event = jsonMapper.fromJson(supportedEventType, payload);

        messageHandler.handle(event);
    }

    public void consumeSqsMessages(
        List<org.springframework.messaging.Message<String>> receivedMessages) {
        List<CompletableFuture<Void>> futures = receivedMessages.stream()
            .map(this::processMessageAndDelete)
            .toList();

        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();
    }

    private void deleteMessage(org.springframework.messaging.Message<?> message) {
        String receiptHandle = (String) message.getHeaders()
            .get(SqsHeaders.SQS_RECEIPT_HANDLE_HEADER);

        DeleteMessageRequest deleteMessageRequest = DeleteMessageRequest.builder()
            .queueUrl(queueUrl)
            .receiptHandle(receiptHandle)
            .build();
        sqsAsyncClient.deleteMessage(deleteMessageRequest);
    }

}
