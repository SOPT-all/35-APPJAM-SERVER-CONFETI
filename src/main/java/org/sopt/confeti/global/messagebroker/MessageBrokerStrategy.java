package org.sopt.confeti.global.messagebroker;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.Getter;
import org.sopt.confeti.global.messagebroker.handler.EventHandler;
import org.sopt.confeti.global.messagebroker.message.Event;

@Getter
public abstract class MessageBrokerStrategy implements MessageBroker {

    private final Map<String, ? extends EventHandler<? extends Event>> eventHandlers;
    private final Map<Class<? extends Event>, EventHandler<? extends Event>> eventHandlerByEventClass;

    protected MessageBrokerStrategy(List<? extends EventHandler<? extends Event>> eventHandlers) {
        this.eventHandlers = eventHandlers.stream().collect(Collectors.toUnmodifiableMap(
            EventHandler::getSupportedTypeId, Function.identity()));
        this.eventHandlerByEventClass = eventHandlers.stream()
            .collect(Collectors.toUnmodifiableMap(
                EventHandler::getSupportedType, Function.identity()));
    }

    public boolean isSupported(Class<? extends Event> eventClass) {
        Optional<EventHandler<? extends Event>> eventHandler = Optional.ofNullable(
            eventHandlerByEventClass.get(eventClass));
        return eventHandler.isPresent();
    }

}
