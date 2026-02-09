package org.sopt.confeti.domain.music.artist.application.dto;

import java.time.LocalDateTime;
import lombok.Builder;
import org.sopt.confeti.global.annotation.RedisSerializable;
import org.sopt.confeti.global.resolver.music_api.artist.vo.ConfetiArtist;

@Builder
@RedisSerializable
public record ArtistInfo(
    String id,
    String name,
    String artworkUrl,
    LocalDateTime createdAt,
    LocalDateTime updatedAt
) {

    public static ArtistInfo fromConfetiArtist(ConfetiArtist artist) {
        return ArtistInfo.builder()
            .id(artist.getId())
            .name(artist.getName())
            .artworkUrl(artist.getProfileUrl())
            .build();
    }
}
