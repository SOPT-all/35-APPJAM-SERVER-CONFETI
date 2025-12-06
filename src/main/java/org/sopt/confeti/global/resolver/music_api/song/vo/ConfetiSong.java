package org.sopt.confeti.global.resolver.music_api.song.vo;

import static org.sopt.confeti.global.common.constant.SongConstant.ARTWORK_IMG_SIZE;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Transient;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.sopt.confeti.domain.music.song.Song;
import org.sopt.confeti.global.annotation.RedisSerializable;
import org.sopt.confeti.global.util.music.dto.music.AppleMusicMusicArtistsResponse;
import org.sopt.confeti.global.util.music.dto.music.AppleMusicMusicAttributesResponse;
import org.sopt.confeti.global.util.music.dto.music.AppleMusicMusicPreviewResponse;
import org.sopt.confeti.global.util.music.dto.music.AppleMusicMusicRelationshipsResponse;
import org.sopt.confeti.global.util.music.dto.music.AppleMusicMusicResponse;
import org.springframework.web.util.UriComponentsBuilder;

@Embeddable
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@RedisSerializable
public class ConfetiSong {

    @Column(name = "music_id", length = 50, nullable = false)
    private String id;

    @Setter
    @Transient
    private String trackName;

    @Setter
    @Transient
    private String artworkUrl;

    @Setter
    @Transient
    private String artistName;

    @Setter
    @Transient
    private String previewUrl;

    @Transient
    private List<ConfetiSongArtist> artists = new ArrayList<>();

    private ConfetiSong(String musicId) {
        this.id = musicId;
    }

    public static ConfetiSong from(final String musicId) {
        return new ConfetiSong(musicId);
    }

    public static ConfetiSong from(final AppleMusicMusicResponse music) {
        Optional<AppleMusicMusicAttributesResponse> optAttributes = Optional.ofNullable(
            music.attributes());
        String trackName = optAttributes.map(AppleMusicMusicAttributesResponse::name).orElse(null);
        String artworkUrl = optAttributes.map(attributes ->
            UriComponentsBuilder.fromUriString(attributes.artwork().url())
                .buildAndExpand(ARTWORK_IMG_SIZE)
                .toUriString()
        ).orElse(null);
        String artistName = optAttributes.map(AppleMusicMusicAttributesResponse::artistName)
            .orElse(null);
        String previewUrl = optAttributes.flatMap(attributes -> attributes.previews().stream()
            .findFirst()
            .map(AppleMusicMusicPreviewResponse::url)
        ).orElse(null);

        List<ConfetiSongArtist> artists = Optional.ofNullable(music.relationships())
            .map(AppleMusicMusicRelationshipsResponse::artists)
            .map(AppleMusicMusicArtistsResponse::data)
            .map(data ->
                data.stream()
                    .map(ConfetiSongArtist::from)
                    .toList()
            ).orElseGet(List::of);

        return new ConfetiSong(
            music.id(),
            trackName,
            artworkUrl,
            artistName,
            previewUrl
            , artists
        );
    }

    public Song toSong() {
        return Song.builder()
            .songId(id)
            .trackName(trackName)
            .artworkUrl(artworkUrl)
            .build();
    }
}
