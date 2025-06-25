package org.sopt.confeti.api.auth.dto.response;

import org.sopt.confeti.api.auth.facade.dto.response.GetIsOnboardingDTO;

public record GetIsOnboardingResponse(
        boolean isOnboarding
) {
    public static GetIsOnboardingResponse from(GetIsOnboardingDTO getIsOnboardingDTO) {
        return new GetIsOnboardingResponse(getIsOnboardingDTO.isOnboarding());
    }
}
