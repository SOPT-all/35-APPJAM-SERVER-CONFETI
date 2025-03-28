package org.sopt.confeti.global.util.music.dto.album;

import java.time.LocalDate;

public record AppleMusicAlbumAttributesResponse(
        LocalDate releaseDate,
        String name
) {
}
