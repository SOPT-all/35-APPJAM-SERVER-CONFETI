package org.sopt.confeti.global.notification;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class SlackHighErrorNotificationUrl implements SlackNotificationUrl {

    @Value("${notification.slack.error.high.url}")
    private String webhookUrl;
    
    @Override
    public SlackNotificationType getType() {
        return SlackNotificationType.HIGH_ERROR;
    }

    @Override
    public String getUrl() {
        return webhookUrl;
    }
}
