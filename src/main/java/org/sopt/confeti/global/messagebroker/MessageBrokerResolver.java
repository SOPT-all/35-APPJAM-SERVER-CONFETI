package org.sopt.confeti.global.messagebroker;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.sopt.confeti.global.annotation.Resolver;
import org.sopt.confeti.global.messagebroker.message.Event;

@Slf4j
@Resolver
@RequiredArgsConstructor
public class MessageBrokerResolver {

    private final MessageBrokerProvider messageBrokerProvider;

    public void sendMessage(Event event) {
        MessageBrokerStrategy strategy = messageBrokerProvider.getStrategyByEventClass(
            event.getClass());
        strategy.sendEvent(event);
    }

}
