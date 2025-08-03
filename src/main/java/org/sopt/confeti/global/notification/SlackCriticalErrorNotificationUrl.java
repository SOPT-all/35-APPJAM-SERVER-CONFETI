package org.sopt.confeti.global.notification;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile(
        value = { "prod", "dev" }
)
public class SlackCriticalErrorNotificationUrl implements SlackNotificationUrl {

    @Value("${notification.slack.error.critical.url}")
    private String webhookUrl;

    @Override
    public SlackNotificationType getType() {
        return SlackNotificationType.CRITICAL_ERROR;
    }

    @Override
    public String getUrl() {
        return webhookUrl;
    }
}
