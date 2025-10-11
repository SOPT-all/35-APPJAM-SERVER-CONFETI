package org.sopt.confeti.global.messagebroker;

import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.sopt.confeti.global.annotation.Resolver;
import org.sopt.confeti.global.exception.ConfetiException;
import org.sopt.confeti.global.message.ErrorMessage;
import org.sopt.confeti.global.messagebroker.message.Event;

@Slf4j
@Resolver
@RequiredArgsConstructor
public class MessageBrokerResolver {

    private final List<MessageBrokerStrategy> messageBrokerStrategies;

    public void sendMessage(Event event) {
        MessageBrokerStrategy strategy = messageBrokerStrategies.stream()
            .filter(messageBrokerStrategy -> messageBrokerStrategy.isSupported(event.getClass()))
            .findFirst()
            .orElseThrow(() -> {
                log.error("send Message error: {}", event);
                throw new ConfetiException(ErrorMessage.INTERNAL_SERVER_ERROR);
            });

        strategy.sendEvent(event);
    }

}
