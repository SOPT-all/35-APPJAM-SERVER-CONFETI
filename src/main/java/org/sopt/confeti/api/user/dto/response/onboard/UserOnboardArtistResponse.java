package org.sopt.confeti.api.user.dto.response.onboard;

import org.sopt.confeti.api.user.facade.dto.response.onboard.UserOnboardArtistDTO;

public record UserOnboardArtistResponse(
    String artistId,
    String profileUrl,
    String name
) {

    public static UserOnboardArtistResponse from(UserOnboardArtistDTO artistDTO) {
        return new UserOnboardArtistResponse(
            artistDTO.id(),
            artistDTO.profileUrl(),
            artistDTO.name()
        );
    }
}
