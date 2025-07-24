package org.sopt.confeti.domain.user;

import org.sopt.confeti.domain.user.constant.Role;

public enum OnboardStatus {
    PROCESSING,
    COMPLETED;

    public static OnboardStatus get(Role role) {
        return role == Role.ONBOARDING ? PROCESSING : COMPLETED;
    }
}
