package org.sopt.confeti.global.messagebroker;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.sopt.confeti.global.annotation.Resolver;
import org.sopt.confeti.global.exception.ConfetiException;
import org.sopt.confeti.global.message.ErrorMessage;
import org.sopt.confeti.global.messagebroker.message.Event;

@Slf4j
@Resolver
public class MessageBrokerResolver {

    private final Map<Class<? extends Event>, MessageBrokerStrategy> strategyByEventClass;

    protected MessageBrokerResolver(List<MessageBrokerStrategy> messageBrokerStrategies,
        List<Event> events) {
        this.strategyByEventClass = events.stream()
            .collect(Collectors.toUnmodifiableMap(
                    Event::getClass,
                    event -> messageBrokerStrategies.stream()
                        .filter(messageBrokerStrategy ->
                            messageBrokerStrategy.isSupported(event.getClass()))
                        .findFirst()
                        .orElseThrow(() -> {
                            log.error(
                                "[Message broker strategy Initialize Error]: Not Found for Event {}",
                                event.getClass());
                            return new ConfetiException(ErrorMessage.INTERNAL_SERVER_ERROR);
                        })
                )
            );
    }

    public void sendMessage(Event event) {
        MessageBrokerStrategy strategy = strategyByEventClass.get(event.getClass());
        strategy.sendEvent(event);
    }

}
