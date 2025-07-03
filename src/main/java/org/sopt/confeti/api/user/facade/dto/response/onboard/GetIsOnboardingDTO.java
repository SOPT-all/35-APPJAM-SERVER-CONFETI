package org.sopt.confeti.api.user.facade.dto.response.onboard;

import org.sopt.confeti.domain.user.constant.Role;

public record GetIsOnboardingDTO(
        boolean isOnboarding
) {
    public static GetIsOnboardingDTO from(Role userRole) {
        return new GetIsOnboardingDTO(Role.ONBOARDING.equals(userRole));
    }
}
