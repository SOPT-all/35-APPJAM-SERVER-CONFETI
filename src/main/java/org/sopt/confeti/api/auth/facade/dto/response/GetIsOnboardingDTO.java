package org.sopt.confeti.api.auth.facade.dto.response;

import org.sopt.confeti.domain.user.constant.Role;

public record GetIsOnboardingDTO(
        boolean isOnboarding
) {
    public static GetIsOnboardingDTO from(Role userRole) {
        return new GetIsOnboardingDTO(Role.ONBOARDING.equals(userRole));
    }
}
