package org.sopt.confeti.global.util.music.dto.music;

import java.util.List;

public record AppleMusicMusicAttributesResponse(
        String name,
        String artistName,
        AppleMusicMusicArtworkResponse artwork,
        List<AppleMusicMusicPreviewResponse> previews
) {
}
