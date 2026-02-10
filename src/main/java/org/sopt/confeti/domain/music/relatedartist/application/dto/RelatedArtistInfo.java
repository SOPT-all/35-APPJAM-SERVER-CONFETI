package org.sopt.confeti.domain.music.relatedartist.application.dto;

import java.time.LocalDateTime;
import java.util.List;
import lombok.Builder;
import org.sopt.confeti.global.annotation.RedisSerializable;
import org.sopt.confeti.global.resolver.music_api.artist.vo.ConfetiArtist;

@Builder
@RedisSerializable
public record RelatedArtistInfo(
    Long id,
    ConfetiArtist artist,
    ConfetiArtist relatedArtist,
    LocalDateTime createdAt,
    LocalDateTime updatedAt
) {

    public static List<RelatedArtistInfo> fromConfetiArtists(String sourceArtistId,
        List<ConfetiArtist> relatedArtists) {
        ConfetiArtist sourceArtist = ConfetiArtist.builder().id(sourceArtistId).build();
        return relatedArtists.stream()
            .map(relatedArtist -> RelatedArtistInfo.builder()
                .artist(sourceArtist)
                .relatedArtist(relatedArtist)
                .build())
            .toList();
    }
}
