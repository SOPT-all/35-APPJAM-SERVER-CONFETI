package org.sopt.confeti.api.user.dto.response.onboard;

import org.sopt.confeti.api.user.facade.dto.response.onboard.GetIsOnboardingDTO;

public record GetIsOnboardingResponse(
        boolean isOnboarding
) {
    public static GetIsOnboardingResponse from(GetIsOnboardingDTO getIsOnboardingDTO) {
        return new GetIsOnboardingResponse(getIsOnboardingDTO.isOnboarding());
    }
}
