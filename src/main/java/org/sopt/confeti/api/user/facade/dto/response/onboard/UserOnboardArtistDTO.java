package org.sopt.confeti.api.user.facade.dto.response.onboard;


import org.sopt.confeti.global.resolver.music_api.artist.vo.ConfetiArtist;

public record UserOnboardArtistDTO(
    String id,
    String profileUrl,
    String name
) {

    public static UserOnboardArtistDTO from(ConfetiArtist artist) {
        return new UserOnboardArtistDTO(
            artist.getId(),
            artist.getProfileUrl(),
            artist.getName()
        );
    }
}
