package org.sopt.confeti.api.user.facade.dto.response.onboard;

import org.sopt.confeti.global.resolver.music_api.artist.vo.ConfetiArtist;

public record UserOnboardFavoriteArtistDTO(
    String id,
    String profileUrl,
    String name
) {

    public static UserOnboardFavoriteArtistDTO from(ConfetiArtist confetiArtist) {
        return new UserOnboardFavoriteArtistDTO(
            confetiArtist.getId(),
            confetiArtist.getProfileUrl(),
            confetiArtist.getName()
        );
    }

}
