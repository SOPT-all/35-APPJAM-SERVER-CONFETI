package org.sopt.confeti.api.user.dto.response.onboard;

import java.util.List;
import org.sopt.confeti.api.user.facade.dto.response.onboard.UserOnboardRelatedArtistsDTO;

public record UserOnboardRelatedArtistsResponse(
        List<UserOnboardRelatedArtistResponse> artists
) {
    public static UserOnboardRelatedArtistsResponse from(UserOnboardRelatedArtistsDTO artistsDTO) {
        return new UserOnboardRelatedArtistsResponse(
                artistsDTO.artists().stream()
                        .map(UserOnboardRelatedArtistResponse::from)
                        .toList()
        );
    }
}
