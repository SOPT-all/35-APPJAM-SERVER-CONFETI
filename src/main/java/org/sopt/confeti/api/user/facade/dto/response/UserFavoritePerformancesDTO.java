package org.sopt.confeti.api.user.facade.dto.response;

import java.util.List;

public record UserFavoritePerformancesDTO(
        List<UserFavoritePerformanceDTO> performances
) {
}
