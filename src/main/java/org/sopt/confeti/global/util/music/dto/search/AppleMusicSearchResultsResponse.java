package org.sopt.confeti.global.util.music.dto.search;

import org.sopt.confeti.global.util.music.dto.artist.AppleMusicArtistsResponse;

public record AppleMusicSearchResultsResponse(
        AppleMusicArtistsResponse artists
) {
}
