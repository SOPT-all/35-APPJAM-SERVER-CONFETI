package org.sopt.confeti.global.util.music.dto.search;

import org.sopt.confeti.global.util.music.dto.artist.AppleMusicArtistsResponse;
import org.sopt.confeti.global.util.music.dto.music.AppleMusicMusicsResponse;

public record AppleMusicSearchResultsResponse(
        AppleMusicArtistsResponse artists,
        AppleMusicMusicsResponse songs
) {
}
