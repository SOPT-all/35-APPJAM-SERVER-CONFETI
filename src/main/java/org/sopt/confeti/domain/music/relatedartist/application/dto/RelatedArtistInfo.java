package org.sopt.confeti.domain.music.relatedartist.application.dto;

import java.time.LocalDateTime;
import java.util.List;
import lombok.Builder;
import org.sopt.confeti.domain.music.artist.application.dto.ArtistInfo;
import org.sopt.confeti.global.annotation.RedisSerializable;
import org.sopt.confeti.global.resolver.music_api.artist.vo.ConfetiArtist;

@Builder
@RedisSerializable
public record RelatedArtistInfo(
    Long id,
    ArtistInfo artist,
    ArtistInfo relatedArtist,
    LocalDateTime createdAt,
    LocalDateTime updatedAt
) {

    public static List<RelatedArtistInfo> fromConfetiArtists(String sourceArtistId, List<ConfetiArtist> relatedArtists) {
        ArtistInfo sourceArtist = ArtistInfo.builder().id(sourceArtistId).build();
        return relatedArtists.stream()
            .map(ca -> RelatedArtistInfo.builder()
                .artist(sourceArtist)
                .relatedArtist(ArtistInfo.fromConfetiArtist(ca))
                .build())
            .toList();
    }

    public ConfetiArtist toConfetiArtist() {
        return ConfetiArtist.of(relatedArtist.id(), relatedArtist.name(), relatedArtist.artworkUrl());
    }
}
