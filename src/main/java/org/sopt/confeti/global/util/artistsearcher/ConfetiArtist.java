package org.sopt.confeti.global.util.artistsearcher;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Transient;
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

    @Column(length = 100)
    private String artistId;

    @Transient
    private String name;

    @Transient
    private String profileUrl;

    public static ConfetiArtist toConfetiArtist(final Artist artist) {
        Optional<Image> image = Arrays.stream(artist.getImages())
                .min(Comparator.comparingInt(Image::getHeight));

        String profileUrl = null;

        if (image.isPresent()) {
            profileUrl = image.get().getUrl();
        }

        return new ConfetiArtist(
                artist.getId(),
                artist.getName(),
                profileUrl
        );
    }

    public ConfetiArtist(String artistId) {
        this.artistId = artistId;
    }
}
