package org.sopt.confeti.api.user.dto.response.onboard;

import org.sopt.confeti.api.user.facade.dto.response.onboard.GetOnboardStatusDTO;
import org.sopt.confeti.domain.user.OnboardStatus;

public record GetOnboardStatusResponse(
        OnboardStatus onboardStatus
) {
    public static GetOnboardStatusResponse from(GetOnboardStatusDTO getOnboardStatusDTO) {
        return new GetOnboardStatusResponse(getOnboardStatusDTO.onboardStatus());
    }
}
