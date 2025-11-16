package org.sopt.confeti.global.messagebroker;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.Getter;
import org.sopt.confeti.global.messagebroker.handler.MessageHandler;
import org.sopt.confeti.global.messagebroker.message.Message;

@Getter
public abstract class MessageBrokerStrategy implements MessagePublisher {

    private final Map<String, ? extends MessageHandler<? extends Message>> messageHandlers;
    private final Map<Class<? extends Message>, MessageHandler<? extends Message>> messageHandlerByMessageClass;

    protected MessageBrokerStrategy(
        List<? extends MessageHandler<? extends Message>> messageHandlers) {
        this.messageHandlers = messageHandlers.stream().collect(Collectors.toUnmodifiableMap(
            MessageHandler::getSupportedTypeId, Function.identity()));
        this.messageHandlerByMessageClass = messageHandlers.stream()
            .collect(Collectors.toUnmodifiableMap(
                MessageHandler::getSupportedType, Function.identity()));
    }

}
