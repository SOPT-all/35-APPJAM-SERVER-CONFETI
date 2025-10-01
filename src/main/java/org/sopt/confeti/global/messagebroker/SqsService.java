package org.sopt.confeti.global.messagebroker;

import static org.sopt.confeti.global.message.ErrorMessage.EVENT_NOT_FOUND;

import io.awspring.cloud.sqs.annotation.SqsListener;
import io.awspring.cloud.sqs.operations.SqsTemplate;
import jakarta.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.Consumer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.sopt.confeti.domain.applemusic.artist.application.ArtistService;
import org.sopt.confeti.domain.applemusic.artist.event.CreateArtistEvent;
import org.sopt.confeti.domain.applemusic.song.application.SongService;
import org.sopt.confeti.domain.applemusic.song.event.CreateSongEvent;
import org.sopt.confeti.global.exception.ConfetiException;
import org.sopt.confeti.global.notification.NotificationAgent;
import org.sopt.confeti.global.notification.SlackNotificationType;
import org.sopt.confeti.global.util.JsonUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import software.amazon.awssdk.services.sqs.model.Message;
import software.amazon.awssdk.services.sqs.model.MessageAttributeValue;

@Slf4j
@Service
@RequiredArgsConstructor
public class SqsService implements MessageBroker {

    @Value("${event.queues.confeti-server}")
    private String queueName;

    private static final String ATTRIBUTE_DATA_TYPE_STRING = "string";
    private static final String ATTRIBUTE_EVENT_TYPE = "event";
    private static final String ATTRIBUTE_TRACE_ID = "traceId";
    private static final String ATTRIBUTE_TYPE_ID = "confetiEventType";

    private final ArtistService artistService;
    private final SongService songService;

    private final SqsTemplate sqsTemplate;
    private final NotificationAgent notificationAgent;

    private final Map<String, Consumer<String>> eventHandlers = new HashMap<>();


    @PostConstruct
    private void init() {
        eventHandlers.put(getTypeId(CreateSongEvent.class), this::handleCreateSongEvent);
        eventHandlers.put(getTypeId(CreateArtistEvent.class), this::handleCreateArtistEvent);
    }

    @Override
    public void sendCreateArtistEvent(CreateArtistEvent event) {
        asyncSend(queueName, event);
    }

    @Override
    public void sendCreateSongEvent(CreateSongEvent event) {
        asyncSend(queueName, event);
    }

    @Transactional
    @SqsListener(value = "${event.queues.confeti-server}")
    public void handleEvent(Message message) {
        String payload = message.body();
        Map<String, MessageAttributeValue> messageAttributes = message.messageAttributes();
        String type = messageAttributes.get(ATTRIBUTE_TYPE_ID).stringValue();

        consumeEvent(type, payload);
    }

    private <T> void asyncSend(String queueName, T data) {
        Map<String, Object> messageAttributes = createMessageAttributes(data);
        String serializedData = JsonUtil.toJson(data);

        sqsTemplate.sendAsync(to -> to
                .queue(queueName)
                .payload(serializedData)
                .headers(messageAttributes)
            )
            .exceptionally(exception -> {
                String errorMessage = extractExceptionMessage(exception, data);
                notificationAgent.notify(SlackNotificationType.HIGH_ERROR, errorMessage);
                log.error("[SqsService send exception] {}", errorMessage, exception);
                return null;
            });
    }

    private <T> Map<String, Object> createMessageAttributes(T data) {
        String eventType = data.getClass().getSimpleName();
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
            ATTRIBUTE_TYPE_ID, getTypeId(data)
        );
    }

    private <T> String extractExceptionMessage(Throwable exception, T data) {
        return String.format("[SQS Exception] Exception Message: %s, Data: %s",
            exception.getMessage(),
            data.toString());
    }

    private void consumeEvent(String type, String payload) {
        Consumer<String> typeConsumer = eventHandlers.get(type);
        if (typeConsumer == null) {
            log.error("Cannot find event handler: {}. Payload: {}", type, payload);
            throw new ConfetiException(EVENT_NOT_FOUND);
        }

        typeConsumer.accept(payload);
    }

    private void handleCreateSongEvent(String payload) {
        CreateSongEvent event = JsonUtil.fromJson(CreateSongEvent.class, payload);
        if (!songService.isExistBySongId(event.songId())) {
            songService.create(event.toSong());
        }
    }

    private void handleCreateArtistEvent(String payload) {
        CreateArtistEvent event = JsonUtil.fromJson(CreateArtistEvent.class, payload);
        if (!artistService.isExistByArtistId(event.artistId())) {
            artistService.create(event.toArtist());
        }
    }

    private static <T> String getTypeId(T data) {
        return getTypeId(data.getClass());
    }

    private static <T> String getTypeId(Class<T> clazz) {
        return clazz.getName();
    }
}
