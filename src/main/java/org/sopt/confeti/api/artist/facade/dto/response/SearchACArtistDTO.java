package org.sopt.confeti.api.artist.facade.dto.response;

import org.sopt.confeti.global.resolver.music_api.artist.vo.ConfetiArtist;

public record SearchACArtistDTO(
        String id,
        String name,
        String profileUrl
) {
    public static SearchACArtistDTO from(ConfetiArtist artist) {
        return new SearchACArtistDTO(
                artist.getId(),
                artist.getName(),
                artist.getProfileUrl()
        );
    }
}
