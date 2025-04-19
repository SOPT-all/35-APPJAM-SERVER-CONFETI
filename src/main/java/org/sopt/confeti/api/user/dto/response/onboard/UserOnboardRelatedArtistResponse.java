package org.sopt.confeti.api.user.dto.response.onboard;

import org.sopt.confeti.api.user.facade.dto.response.onboard.UserOnboardRelatedArtistDTO;

public record UserOnboardRelatedArtistResponse(
        String artistId,
        String profileUrl,
        String name
) {
    public static UserOnboardRelatedArtistResponse from(UserOnboardRelatedArtistDTO artistDTO) {
        return new UserOnboardRelatedArtistResponse(
                artistDTO.id(),
                artistDTO.profileUrl(),
                artistDTO.name()
        );
    }
}
