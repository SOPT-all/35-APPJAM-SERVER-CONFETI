package org.sopt.confeti.domain.user;

import org.sopt.confeti.domain.user.constant.Role;

public enum OnboardStatus {
    PROCESSING,
    COMPLETED;

    public static OnboardStatus get(Role role) {
        if (role.equals(Role.ONBOARDING)) {
            return PROCESSING;
        }

        return COMPLETED;
    }
}
