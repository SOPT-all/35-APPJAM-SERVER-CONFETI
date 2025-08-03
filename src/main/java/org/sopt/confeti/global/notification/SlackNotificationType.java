package org.sopt.confeti.global.notification;

public enum SlackNotificationType implements NotificationType {
    CRITICAL_ERROR, HIGH_ERROR;

    public String getTitle() {
        return switch (this) {
            case CRITICAL_ERROR -> "🔴 Critical Error";
            case HIGH_ERROR -> "🟠 High Error";
        };
    }
}
