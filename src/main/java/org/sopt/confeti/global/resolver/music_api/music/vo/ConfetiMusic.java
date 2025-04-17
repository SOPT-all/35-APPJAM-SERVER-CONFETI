package org.sopt.confeti.global.resolver.music_api.music.vo;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Transient;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.sopt.confeti.global.common.constant.MusicConstant;
import org.sopt.confeti.global.util.music.dto.music.AppleMusicMusicPreviewResponse;
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
    private String title;

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
        List<ConfetiMusicArtist> artists = new ArrayList<>();

        if (Objects.nonNull(music.relationships())) {
            artists = music.relationships().artists().data().stream()
                    .map(ConfetiMusicArtist::from)
                    .toList();
        }

        return new ConfetiMusic(
                music.id(),
                music.attributes().name(),
                UriComponentsBuilder.fromUriString(music.attributes().artwork().url())
                        .buildAndExpand(MusicConstant.ARTWORK_IMG_SIZE)
                        .toUriString(),
                music.attributes().artistName(),
                music.attributes().previews().stream()
                        .findFirst()
                        .map(AppleMusicMusicPreviewResponse::url)
                        .orElse(null),
                artists
        );
    }

    public static ConfetiMusic empty() {
        return new ConfetiMusic();
    }
}
