package org.sopt.confeti.global.util.music.dto.music;

import java.util.List;

public record AppleMusicArtistMusicsResponse(
        String next,
        List<AppleMusicMusicResponse> data
) {
}
