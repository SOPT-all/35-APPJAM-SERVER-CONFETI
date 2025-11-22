package org.sopt.confeti.domain.applemusic.artist.application.dto.request;

import lombok.Builder;
import org.sopt.confeti.domain.applemusic.artist.Artist;

@Builder
public record CreateArtistDTO(
    String artistId,
    String name,
    String artworkUrl
) {

    public Artist toArtist() {
        return Artist.builder()
            .artistId(artistId)
            .name(name)
            .artworkUrl(artworkUrl)
            .build();
    }
}
