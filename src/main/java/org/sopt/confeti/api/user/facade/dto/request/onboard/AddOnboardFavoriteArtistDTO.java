package org.sopt.confeti.api.user.facade.dto.request.onboard;


import org.sopt.confeti.global.resolver.music_api.artist.vo.ConfetiArtist;

public record AddOnboardFavoriteArtistDTO(
    String artistId,
    String name,
    String profileUrl
) {

    public ConfetiArtist toConfetiArtist() {
        return ConfetiArtist.of(artistId, name, profileUrl);
    }
}
