package org.sopt.confeti.api.user.dto.response;

import java.util.List;
import org.sopt.confeti.api.user.facade.dto.response.UserOnboardTopArtistsDTO;

public record UserOnboardTopArtistsResponse(
        List<UserOnboardTopArtistResponse> artists
) {
    public static UserOnboardTopArtistsResponse from(UserOnboardTopArtistsDTO topArtistsDTO) {
        return new UserOnboardTopArtistsResponse(
                topArtistsDTO.artists().stream()
                        .map(UserOnboardTopArtistResponse::from)
                        .toList()
        );
    }
}
