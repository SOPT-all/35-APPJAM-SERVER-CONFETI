package org.sopt.confeti.global.messagebroker;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.sopt.confeti.global.exception.ConfetiException;
import org.sopt.confeti.global.message.ErrorMessage;
import org.sopt.confeti.global.messagebroker.message.Message;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public final class MessageStrategyProvider {

    private final Map<Class<? extends Message>, MessageBrokerStrategy> strategyByMessageClass;

    private MessageStrategyProvider(List<MessageBrokerStrategy> messageBrokerStrategies) {
        this.strategyByMessageClass = messageBrokerStrategies.stream()
            .flatMap(strategy ->
                strategy.getMessageHandlerByMessageClass().keySet().stream()
                    .map(messageClass -> Map.entry(messageClass, strategy))
            )
            .collect(Collectors.toUnmodifiableMap(
                Entry::getKey,
                Entry::getValue,
                (strategy1, strategy2) -> {
                    log.error("Duplicate message class in {}, {}", strategy1.getClass(),
                        strategy2.getClass());
                    throw new ConfetiException(ErrorMessage.INTERNAL_SERVER_ERROR);
                }
            ));
    }

    public MessageBrokerStrategy getStrategyByMessageClass(Class<? extends Message> messageClass) {
        MessageBrokerStrategy strategy = strategyByMessageClass.get(messageClass);
        if (strategy == null) {
            log.error("No strategy about message: {}", messageClass.getName());
            throw new ConfetiException(ErrorMessage.INTERNAL_SERVER_ERROR);
        }
        return strategy;
    }

}
