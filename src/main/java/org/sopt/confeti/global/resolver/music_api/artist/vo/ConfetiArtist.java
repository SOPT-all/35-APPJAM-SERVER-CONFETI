package org.sopt.confeti.global.resolver.music_api.artist.vo;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Transient;
import java.util.Objects;
import java.util.Optional;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.sopt.confeti.domain.music.artist.Artist;
import org.sopt.confeti.global.annotation.RedisSerializable;
import org.sopt.confeti.global.common.constant.ArtistConstant;
import org.sopt.confeti.global.util.music.dto.artist.AppleMusicArtistArtworkResponse;
import org.sopt.confeti.global.util.music.dto.artist.AppleMusicArtistAttributesResponse;
import org.sopt.confeti.global.util.music.dto.artist.AppleMusicArtistResponse;
import org.springframework.web.util.UriComponentsBuilder;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@RedisSerializable
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
        Optional<AppleMusicArtistAttributesResponse> optAttributes = Optional.ofNullable(
            artist.attributes());
        String name = optAttributes.map(AppleMusicArtistAttributesResponse::name).orElse(null);
        String profileUrl = optAttributes.map(AppleMusicArtistAttributesResponse::artwork)
            .map(AppleMusicArtistArtworkResponse::url)
            .map(url -> UriComponentsBuilder.fromUriString(url)
                .buildAndExpand(ArtistConstant.PROFILE_IMG_SIZE)
                .toUriString()
            ).orElse(null);

        return new ConfetiArtist(
            artist.id(),
            name,
            profileUrl
        );
    }

    public static ConfetiArtist from(final String artistId) {
        return new ConfetiArtist(artistId);
    }

    public static ConfetiArtist empty() {
        return new ConfetiArtist();
    }

    public static ConfetiArtist of(final String artistId, final String name,
        final String profileUrl) {
        return new ConfetiArtist(artistId, name, profileUrl);
    }

    public Artist toArtist() {
        return Artist.create(id, name, profileUrl);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ConfetiArtist that)) {
            return false;
        }
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
