package org.sopt.confeti.global.util.music.dto.artist;

import java.util.List;

public record AppleMusicArtistAttributesResponse(
        List<String> genreNames,
        String name,
        AppleMusicArtistArtworkResponse artwork
) {
}
