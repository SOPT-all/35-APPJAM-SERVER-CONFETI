package org.sopt.confeti.api.user.dto.response.onboard;

import org.sopt.confeti.api.user.facade.dto.response.onboard.UserOnboardRelatedArtistDTO;

public record UserOnboardRelatedArtistResponse(
        String artistId,
        String profileUrl,
        String name
) {
    public static UserOnboardRelatedArtistResponse from(UserOnboardRelatedArtistDTO relatedArtistDTO) {
        return new UserOnboardRelatedArtistResponse(
                relatedArtistDTO.artistId(),
                relatedArtistDTO.profileUrl(),
                relatedArtistDTO.name()
        );
    }
}
