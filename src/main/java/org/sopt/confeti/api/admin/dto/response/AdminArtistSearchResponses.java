package org.sopt.confeti.api.admin.dto.response;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.sopt.confeti.global.common.constant.ArtistConstant;
import org.sopt.confeti.global.util.music.dto.artist.AppleMusicArtistArtworkResponse;
import org.sopt.confeti.global.util.music.dto.artist.AppleMusicArtistAttributesResponse;
import org.sopt.confeti.global.util.music.dto.artist.AppleMusicArtistResponse;
import org.sopt.confeti.global.util.music.dto.search.AppleMusicSearchResponse;
import org.springframework.web.util.UriComponentsBuilder;

public record AdminArtistSearchResponses(
    List<AdminArtistSearchResponse> artists
) {

    public static AdminArtistSearchResponses from(final AppleMusicSearchResponse response) {
        List<AppleMusicArtistResponse> data = Optional.ofNullable(response)
            .map(AppleMusicSearchResponse::results)
            .map(results -> results.artists())
            .map(artists -> artists.data())
            .orElse(Collections.emptyList());

        return new AdminArtistSearchResponses(
            data.stream()
                .map(AdminArtistSearchResponse::from)
                .toList()
        );
    }

    public record AdminArtistSearchResponse(
        String artworkUrl,
        String name
    ) {

        public static AdminArtistSearchResponse from(final AppleMusicArtistResponse artist) {
            Optional<AppleMusicArtistAttributesResponse> optAttributes = Optional.ofNullable(
                artist.attributes());

            String name = optAttributes
                .map(AppleMusicArtistAttributesResponse::name)
                .orElse(null);

            String artworkUrl = optAttributes
                .map(AppleMusicArtistAttributesResponse::artwork)
                .map(AppleMusicArtistArtworkResponse::url)
                .map(url -> UriComponentsBuilder.fromUriString(url)
                    .buildAndExpand(ArtistConstant.PROFILE_IMG_SIZE)
                    .toUriString()
                )
                .orElse(null);

            return new AdminArtistSearchResponse(artworkUrl, name);
        }
    }
}
