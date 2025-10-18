package org.sopt.confeti.global.messagebroker;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
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

    protected MessageBrokerResolver(List<MessageBrokerStrategy> messageBrokerStrategies) {

        this.strategyByEventClass = messageBrokerStrategies.stream()
            .flatMap(strategy ->
                strategy.getEventHandlerByEventClass().keySet().stream()
                    .map(eventClass -> Map.entry(eventClass, strategy))
            )
            .collect(Collectors.toUnmodifiableMap(
                Entry::getKey,
                Entry::getValue,
                (strategy1, strategy2) -> {
                    log.error("Duplicate event class in {}, {}", strategy1.getClass(),
                        strategy2.getClass());
                    throw new ConfetiException(ErrorMessage.INTERNAL_SERVER_ERROR);
                }
            ));
    }

    public void sendMessage(Event event) {
        MessageBrokerStrategy strategy = strategyByEventClass.get(event.getClass());
        strategy.sendEvent(event);
    }

}
