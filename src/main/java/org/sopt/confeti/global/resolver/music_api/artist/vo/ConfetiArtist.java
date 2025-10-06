package org.sopt.confeti.global.resolver.music_api.artist.vo;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Transient;
import java.util.Objects;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.sopt.confeti.global.common.constant.ArtistConstant;
import org.sopt.confeti.global.util.music.dto.artist.AppleMusicArtistResponse;
import org.springframework.web.util.UriComponentsBuilder;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class ConfetiArtist {

    @Column(name = "artist_id", length = 50, nullable = false)
    private String id;

    @Setter
    @Transient
    private String name;

    @Setter
    @Transient
    private String profileUrl;

    private ConfetiArtist(String artistId) {
        this.id = artistId;
    }

    public static ConfetiArtist from(final AppleMusicArtistResponse artist) {
        String profileUrl = null;

        if (Objects.nonNull(artist.attributes().artwork())) {
            profileUrl = UriComponentsBuilder.fromUriString(artist.attributes().artwork().url())
                    .buildAndExpand(ArtistConstant.PROFILE_IMG_SIZE)
                    .toUriString();
        }

        return new ConfetiArtist(
                artist.id(),
                artist.attributes().name(),
                profileUrl
        );
    }

    public static ConfetiArtist from(final String artistId) {
        return new ConfetiArtist(artistId);
    }

    public static ConfetiArtist empty() {
        ConfetiArtist artist = new ConfetiArtist();

        return artist;
    }
}
