package org.sopt.confeti.api.user.dto.response.onboard;

import java.util.List;
import org.sopt.confeti.api.user.facade.dto.response.onboard.UserOnboardArtistsDTO;

public record UserOnboardArtistsResponse(
    List<UserOnboardArtistResponse> artists
) {

    public static UserOnboardArtistsResponse from(UserOnboardArtistsDTO artistsDTO) {
        return new UserOnboardArtistsResponse(
            artistsDTO.artists().stream()
                .map(UserOnboardArtistResponse::from)
                .toList()
        );
    }
}
