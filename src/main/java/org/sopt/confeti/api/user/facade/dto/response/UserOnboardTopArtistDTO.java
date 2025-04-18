package org.sopt.confeti.api.user.facade.dto.response;

import org.sopt.confeti.global.resolver.music_api.artist.vo.ConfetiArtist;

public record UserOnboardTopArtistDTO(
        String id,
        String profileUrl,
        String name
) {
    public static UserOnboardTopArtistDTO from(ConfetiArtist artist) {
        return new UserOnboardTopArtistDTO(
                artist.getId(),
                artist.getProfileUrl(),
                artist.getName()
        );
    }
}
