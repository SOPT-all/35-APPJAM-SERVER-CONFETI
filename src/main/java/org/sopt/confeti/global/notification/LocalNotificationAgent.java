package org.sopt.confeti.global.notification;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@Profile(
    value = {"dev"}
)
public class LocalNotificationAgent implements NotificationAgent {

    @Override
    public void notify(NotificationType type, String message) {
        if (type instanceof SlackNotificationType slackNotificationType) {
            log.error("NotificationType: {}, Message: {} ", slackNotificationType.getTitle(),
                message);
            return;
        }
        log.error(message);
    }
}
