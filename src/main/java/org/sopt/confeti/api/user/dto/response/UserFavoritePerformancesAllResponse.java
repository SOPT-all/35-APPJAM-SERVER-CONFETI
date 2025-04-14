package org.sopt.confeti.api.user.dto.response;

import org.sopt.confeti.api.user.facade.dto.response.UserFavoritePerformancesAllDTO;
import org.sopt.confeti.global.util.S3FileHandler;

import java.util.List;

public record UserFavoritePerformancesAllResponse(
        List<UserFavoritePerformanceAllResponse> performances) {
        public static UserFavoritePerformancesAllResponse of (
                final UserFavoritePerformancesAllDTO performancesDTO, final S3FileHandler s3FileHandler){
        return new UserFavoritePerformancesAllResponse(
                performancesDTO.performances().stream()
                        .map(userFavoritePerformanceAllDTO ->
                                UserFavoritePerformanceAllResponse.of(userFavoritePerformanceAllDTO, s3FileHandler))
                        .toList()
        );
    }
}
