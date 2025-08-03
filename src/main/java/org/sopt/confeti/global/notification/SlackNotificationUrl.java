package org.sopt.confeti.global.notification;

public interface SlackNotificationUrl {

    SlackNotificationType getType();

    String getUrl();
}
