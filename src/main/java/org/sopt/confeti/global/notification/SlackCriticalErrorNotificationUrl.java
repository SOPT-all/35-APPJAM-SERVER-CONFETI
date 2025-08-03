package org.sopt.confeti.global.notification;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
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
