package org.sopt.confeti.global.notification;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile(
        value = "prod"
)
public class SlackHighErrorNotificationUrl implements SlackNotificationUrl {

    @Value("${slack.notification.error.high.url}")
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
