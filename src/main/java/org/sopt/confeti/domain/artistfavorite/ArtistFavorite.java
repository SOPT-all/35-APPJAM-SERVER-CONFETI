package org.sopt.confeti.domain.artistfavorite;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.sopt.confeti.domain.user.User;

@Entity
@Table(name="artist_favorites")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ArtistFavorite {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="artist_favorite_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="user_id")
    private User user;

    @Column(length = 100, nullable = false)
    private String artistId;

    public Long getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public String getArtistId() {
        return artistId;
    }

    @Builder
    private ArtistFavorite(User user, String artistId) {
        this.user = user;
        this.artistId = artistId;
    }

    public static ArtistFavorite create(User user, String artistId) {
        return ArtistFavorite.builder()
                .user(user)
                .artistId(artistId)
                .build();
    }
}
