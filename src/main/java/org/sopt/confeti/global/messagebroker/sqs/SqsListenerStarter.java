package org.sopt.confeti.global.messagebroker.sqs;

import io.awspring.cloud.sqs.listener.DefaultListenerContainerRegistry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class SqsListenerStarter implements ApplicationListener<ApplicationReadyEvent> {

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    private final DefaultListenerContainerRegistry registry;

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        log.info("registered SQS Listener Container count: {}",
            registry.getListenerContainers().size());
        registry.getListenerContainers().forEach(c -> {
            log.info("SQS Listener Container [{}] starting...", c.getId());
            c.start();
            log.info("SQS Listener Container [{}] running: {}", c.getId(), c.isRunning());
        });
    }
}
