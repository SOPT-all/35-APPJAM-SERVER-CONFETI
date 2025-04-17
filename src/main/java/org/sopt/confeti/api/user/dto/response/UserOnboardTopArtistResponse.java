package org.sopt.confeti.api.user.dto.response;

import org.sopt.confeti.api.user.facade.dto.response.UserOnboardTopArtistDTO;

public record UserOnboardTopArtistResponse(
        String artistId,
        String profileUrl,
        String name
) {
    public static UserOnboardTopArtistResponse from(UserOnboardTopArtistDTO topArtistDTO) {
        return new UserOnboardTopArtistResponse(
                topArtistDTO.id(),
                topArtistDTO.profileUrl(),
                topArtistDTO.name()
        );
    }
}
