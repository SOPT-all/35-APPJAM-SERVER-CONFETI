package org.sopt.confeti.api.admin.dto.response;

import java.util.List;
import org.sopt.confeti.global.resolver.music_api.artist.vo.ConfetiArtist;

public record AdminArtistSearchResponses(
    List<AdminArtistSearchResponse> artists
) {

    public static AdminArtistSearchResponses from(final List<ConfetiArtist> artists) {
        return new AdminArtistSearchResponses(
            artists.stream()
                .map(AdminArtistSearchResponse::from)
                .toList()
        );
    }

    public record AdminArtistSearchResponse(
        String id,
        String name,
        String artworkUrl
    ) {

        public static AdminArtistSearchResponse from(final ConfetiArtist artist) {
            return new AdminArtistSearchResponse(artist.getId(), artist.getName(), artist.getProfileUrl());
        }
    }
}
