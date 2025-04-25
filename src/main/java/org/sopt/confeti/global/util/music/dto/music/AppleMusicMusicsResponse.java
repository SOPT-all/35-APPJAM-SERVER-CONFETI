package org.sopt.confeti.global.util.music.dto.music;

import java.util.List;

public record AppleMusicMusicsResponse(
        String next,
        List<AppleMusicMusicResponse> data
) {
}
