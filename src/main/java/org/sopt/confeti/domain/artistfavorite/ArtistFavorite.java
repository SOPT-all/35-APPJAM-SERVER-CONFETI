package org.sopt.confeti.domain.artistfavorite;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.sopt.confeti.domain.user.User;
import org.sopt.confeti.global.util.artistsearcher.ConfetiArtist;

@Entity
@Table(name="artist_favorites")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ArtistFavorite {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="artist_favorite_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="user_id")
    private User user;

    @Embedded
    private ConfetiArtist artist;

    @Builder
    private ArtistFavorite(User user, String artistId) {
        this.user = user;
        this.artist = new ConfetiArtist(artistId);
    }

    public static ArtistFavorite create(User user, String artistId) {
        return ArtistFavorite.builder()
                .user(user)
                .artistId(artistId)
                .build();
    }
}
