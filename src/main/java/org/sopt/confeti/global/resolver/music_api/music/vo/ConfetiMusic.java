package org.sopt.confeti.global.resolver.music_api.music.vo;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Transient;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.sopt.confeti.global.common.constant.MusicConstant;
import org.sopt.confeti.global.util.music.dto.music.AppleMusicMusicArtistsResponse;
import org.sopt.confeti.global.util.music.dto.music.AppleMusicMusicAttributesResponse;
import org.sopt.confeti.global.util.music.dto.music.AppleMusicMusicPreviewResponse;
import org.sopt.confeti.global.util.music.dto.music.AppleMusicMusicRelationshipsResponse;
import org.sopt.confeti.global.util.music.dto.music.AppleMusicMusicResponse;
import org.springframework.web.util.UriComponentsBuilder;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class ConfetiMusic {

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
    private List<ConfetiMusicArtist> artists = new ArrayList<>();

    private ConfetiMusic(String musicId) {
        this.id = musicId;
    }

    public static ConfetiMusic from(final String musicId) {
        return new ConfetiMusic(musicId);
    }

    public static ConfetiMusic from(final AppleMusicMusicResponse music) {
        Optional<AppleMusicMusicAttributesResponse> optAttributes = Optional.ofNullable(music.attributes());
        String trackName = optAttributes.map(AppleMusicMusicAttributesResponse::name).orElse(null);
        String artworkUrl = optAttributes.map(attributes ->
                        UriComponentsBuilder.fromUriString(attributes.artwork().url())
                        .buildAndExpand(MusicConstant.ARTWORK_IMG_SIZE)
                        .toUriString()
        ).orElse(null);
        String artistName = optAttributes.map(AppleMusicMusicAttributesResponse::artistName).orElse(null);
        String previewUrl = optAttributes.flatMap(attributes -> attributes.previews().stream()
                .findFirst()
                .map(AppleMusicMusicPreviewResponse::url)
        ).orElse(null);

        List<ConfetiMusicArtist> artists = Optional.ofNullable(music.relationships())
                .map(AppleMusicMusicRelationshipsResponse::artists)
                .map(AppleMusicMusicArtistsResponse::data)
                .map(data ->
                        data.stream()
                                .map(ConfetiMusicArtist::from)
                                .toList()
                ).orElseGet(List::of);

        return new ConfetiMusic(
                music.id(),
                trackName,
                artworkUrl,
                artistName,
                previewUrl
                ,artists
        );
    }
}
