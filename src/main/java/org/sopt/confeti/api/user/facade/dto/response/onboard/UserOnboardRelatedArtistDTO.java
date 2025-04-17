package org.sopt.confeti.api.user.facade.dto.response.onboard;

import org.sopt.confeti.global.resolver.music_api.artist.vo.ConfetiArtist;

public record UserOnboardRelatedArtistDTO(
        String artistId,
        String profileUrl,
        String name
) {
    public static UserOnboardRelatedArtistDTO from(ConfetiArtist artist) {
        return new UserOnboardRelatedArtistDTO(
                artist.getId(),
                artist.getProfileUrl(),
                artist.getName()
        );
    }
}
