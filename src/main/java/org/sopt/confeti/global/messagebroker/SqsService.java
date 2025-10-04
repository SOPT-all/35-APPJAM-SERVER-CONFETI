package org.sopt.confeti.global.messagebroker;

import static org.sopt.confeti.global.config.ThreadPoolConfig.MESSAGE_CONSUMER_POOL;
import static org.sopt.confeti.global.message.ErrorMessage.INTERNAL_SERVER_ERROR;

import io.awspring.cloud.sqs.annotation.SqsListener;
import io.awspring.cloud.sqs.listener.acknowledgement.Acknowledgement;
import io.awspring.cloud.sqs.operations.SqsTemplate;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.sopt.confeti.domain.applemusic.artist.event.CreateArtistEvent;
import org.sopt.confeti.domain.applemusic.song.event.CreateSongEvent;
import org.sopt.confeti.global.exception.ConfetiException;
import org.sopt.confeti.global.messagebroker.handler.EventHandler;
import org.sopt.confeti.global.notification.NotificationAgent;
import org.sopt.confeti.global.notification.SlackNotificationType;
import org.sopt.confeti.global.util.JsonUtil;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.task.TaskExecutor;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.sqs.model.MessageAttributeValue;

@Slf4j
@Service
public class SqsService implements MessageBroker {

    @Value("${event.queues.confeti-server}")
    private String queueName;

    private static final String ATTRIBUTE_DATA_TYPE_STRING = "string";
    private static final String ATTRIBUTE_EVENT_TYPE = "event";
    private static final String ATTRIBUTE_TRACE_ID = "traceId";
    private static final String ATTRIBUTE_TYPE_ID = "confetiEventType";

    private final SqsTemplate sqsTemplate;
    private final NotificationAgent notificationAgent;

    private final Map<String, EventHandler<? extends Event>> eventHandlers;
    private final TaskExecutor messageConsumeExecutor;

    public SqsService(
        List<EventHandler<? extends Event>> allEventHandlers,
        @Qualifier(MESSAGE_CONSUMER_POOL) TaskExecutor messageConsumeExecutor,
        SqsTemplate sqsTemplate,
        NotificationAgent notificationAgent
    ) {
        this.eventHandlers = allEventHandlers.stream().collect(Collectors.toUnmodifiableMap(
            EventHandler::getSupportedTypeId, eventHandler -> eventHandler));
        this.messageConsumeExecutor = messageConsumeExecutor;
        this.sqsTemplate = sqsTemplate;
        this.notificationAgent = notificationAgent;
    }

    @Override
    public void sendCreateArtistEvent(CreateArtistEvent event) {
        asyncSend(queueName, event);
    }

    @Override
    public void sendCreateSongEvent(CreateSongEvent event) {
        asyncSend(queueName, event);
    }

    @SqsListener(value = "${event.queues.confeti-server}")
    public CompletableFuture<Void> consumeEvent(List<Message<String>> messages) {
        List<CompletableFuture<Void>> futures = messages.stream()
            .map(message -> {
                return CompletableFuture.runAsync(() -> {
                        String type = message.getHeaders().get(ATTRIBUTE_TYPE_ID, String.class);
                        String payload = message.getPayload().toString();
                        handleEvent(type, payload);
                    }, messageConsumeExecutor)
                    .thenRun(() -> Acknowledgement.acknowledge(message))
                    .exceptionally(e -> {
                        log.error("message: {}", message.toString(), e);
                        return null;
                    });
            })
            .toList();

        return CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]));
    }


    private void asyncSend(String queueName, Event event) {
        Map<String, Object> messageAttributes = createMessageAttributes(event);
        String serializedData = JsonUtil.toJson(event);

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
            SqsService.ATTRIBUTE_EVENT_TYPE, MessageAttributeValue.builder()
                .stringValue(eventType)
                .dataType(SqsService.ATTRIBUTE_DATA_TYPE_STRING)
                .build(),
            SqsService.ATTRIBUTE_TRACE_ID, MessageAttributeValue.builder()
                .stringValue(eventTraceId)
                .dataType(SqsService.ATTRIBUTE_DATA_TYPE_STRING)
                .build(),
            ATTRIBUTE_TYPE_ID, event.getTypeId()
        );
    }

    private <T> String extractExceptionMessage(Throwable exception, T data) {
        return String.format("[SQS Exception] Exception Message: %s, Data: %s",
            exception.getMessage(),
            data.toString());
    }

    private void handleEvent(String type, String payload) {
        EventHandler<? extends Event> eventHandler = eventHandlers.get(type);
        if (eventHandler == null) {
            log.error("Cannot find event handler: {}. Payload: {}", type, payload);
            throw new ConfetiException(INTERNAL_SERVER_ERROR);
        }

        processEvent(eventHandler, payload);
    }

    private <T extends Event> void processEvent(EventHandler<T> eventHandler, String payload) {
        Class<T> supportedEventType = eventHandler.getSupportedEventType();
        T event = JsonUtil.fromJson(supportedEventType, payload);

        eventHandler.handle(event);
    }

}
