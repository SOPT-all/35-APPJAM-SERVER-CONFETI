package org.sopt.confeti.global.resolver.artist.vo;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Transient;
import java.util.Optional;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.sopt.confeti.global.common.constant.ArtistConstant;
import org.sopt.confeti.global.util.music.dto.artist.AppleMusicArtistAlbumResponse;
import org.sopt.confeti.global.util.music.dto.artist.AppleMusicArtistResponse;
import org.springframework.web.util.UriComponentsBuilder;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class ConfetiArtist {

    @Column(length = 50, nullable = false)
    private String artistId;

    @Setter
    @Transient
    private String name;

    @Setter
    @Transient
    private String profileUrl;

    @Setter
    @Transient
    private ConfetiAlbum latestReleaseAlbum;

    private ConfetiArtist(String artistId) {
        this.artistId = artistId;
    }

    public static ConfetiArtist from(final AppleMusicArtistResponse artist) {
        Optional<AppleMusicArtistAlbumResponse> album = artist
                .relationships()
                .albums()
                .data()
                .stream().findFirst();

        return new ConfetiArtist(
                artist.id(),
                artist.attributes().name(),
                UriComponentsBuilder.fromUriString(artist.attributes().artwork().url())
                        .buildAndExpand(ArtistConstant.PROFILE_IMG_SIZE)
                        .toUriString(),
                album.map(ConfetiAlbum::from).orElse(null)
        );
    }

    public static ConfetiArtist from(final String artistId) {
        return new ConfetiArtist(artistId);
    }

    public static ConfetiArtist empty() {
        return new ConfetiArtist();
    }
}
