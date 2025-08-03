package org.sopt.confeti.global.notification;

public interface NotificationAgent {

    void notify(NotificationType type, String message);
}
