package org.sopt.confeti.global.messagebroker;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.sopt.confeti.global.messagebroker.handler.MessageHandler;
import org.sopt.confeti.global.messagebroker.message.Message;

public abstract class MessageBrokerStrategy implements MessagePublisher {

    private final Map<String, ? extends MessageHandler<? extends Message>> messageHandlerByTypeId;
    private final Map<Class<? extends Message>, MessageHandler<? extends Message>> messageHandlerByMessageClass;

    protected MessageBrokerStrategy(
        List<? extends MessageHandler<? extends Message>> messageHandlers) {
        this.messageHandlerByTypeId = messageHandlers.stream().collect(Collectors.toUnmodifiableMap(
            MessageHandler::getSupportedTypeId, Function.identity()));
        this.messageHandlerByMessageClass = messageHandlers.stream()
            .collect(Collectors.toUnmodifiableMap(
                MessageHandler::getSupportedType, Function.identity()));
    }

    public final Set<Class<? extends Message>> getSupportedMessageClasses() {
        return messageHandlerByMessageClass.keySet();
    }

    protected final MessageHandler<? extends Message> getHandlerByMessageClass(
        Class<? extends Message> messageClass) {
        return messageHandlerByMessageClass.get(messageClass);

    }

    protected final MessageHandler<? extends Message> getHandlerByTypeId(String typeId) {
        return messageHandlerByTypeId.get(typeId);
    }

}
