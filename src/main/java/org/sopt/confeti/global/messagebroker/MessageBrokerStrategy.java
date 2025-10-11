package org.sopt.confeti.global.messagebroker;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.Getter;
import org.sopt.confeti.global.messagebroker.handler.EventHandler;
import org.sopt.confeti.global.messagebroker.message.Event;

@Getter
public abstract class MessageBrokerStrategy implements MessageBroker {

    private final Map<String, ? extends EventHandler<? extends Event>> eventHandlers;

    protected MessageBrokerStrategy(List<? extends EventHandler<? extends Event>> eventHandlers) {
        this.eventHandlers = eventHandlers.stream().collect(Collectors.toUnmodifiableMap(
            EventHandler::getSupportedTypeId, Function.identity()));
    }

    public boolean isSupported(Class<? extends Event> eventClass) {
        return eventHandlers.values()
            .stream()
            .anyMatch(eventHandler -> eventHandler.isSupported(eventClass));
    }

}
