package org.sopt.confeti.api.user.facade.dto.response.onboard;

import org.sopt.confeti.domain.user.OnboardStatus;
import org.sopt.confeti.domain.user.constant.Role;

public record GetOnboardStatusDTO(
        OnboardStatus onboardStatus
) {
    public static GetOnboardStatusDTO from(Role userRole) {
        return new GetOnboardStatusDTO(OnboardStatus.get(userRole));
    }
}
