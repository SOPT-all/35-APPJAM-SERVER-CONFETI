package org.sopt.confeti.global.util.artistsearcher;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Transient;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Optional;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import se.michaelthelin.spotify.model_objects.specification.Artist;
import se.michaelthelin.spotify.model_objects.specification.Image;

@Embeddable
@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class ConfetiArtist {

    @Column(length = 100, nullable = false)
    private String artistId;

    @Transient
    private String name;

    @Transient
    private String profileUrl;

    @Transient
    private LocalDate latestReleaseAt;

    public static ConfetiArtist toConfetiArtist(final Artist artist) {
        Optional<Image> image = Arrays.stream(artist.getImages())
                .min(Comparator.comparingInt(Image::getHeight));

        return new ConfetiArtist(
                artist.getId(),
                artist.getName(),
                image.map(Image::getUrl).orElse(null),
                null
        );
    }

    public static ConfetiArtist toConfetiArtist(final Artist artist, final LocalDate latestReleaseAt) {
        Optional<Image> image = Arrays.stream(artist.getImages())
                .min(Comparator.comparingInt(Image::getHeight));

        return new ConfetiArtist(
                artist.getId(),
                artist.getName(),
                image.map(Image::getUrl).orElse(null),
                latestReleaseAt
        );
    }

    public ConfetiArtist(String artistId) {
        this.artistId = artistId;
    }
}
