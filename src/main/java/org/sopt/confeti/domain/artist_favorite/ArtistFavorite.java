package org.sopt.confeti.domain.artist_favorite;

import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.sopt.confeti.domain.user.User;
import org.sopt.confeti.global.resolver.music_api.artist.vo.ConfetiArtist;
import org.springframework.data.annotation.CreatedDate;

@Entity
@Table(name = "artist_favorites")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ArtistFavorite {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @CreatedDate
    private LocalDateTime createdAt;

    @Embedded
    private ConfetiArtist artist;

    @Builder
    private ArtistFavorite(User user, String artistId, LocalDateTime createdAt) {
        this.user = user;
        this.artist = ConfetiArtist.from(artistId);
        this.createdAt = LocalDateTime.now();
    }

    public static ArtistFavorite create(User user, String artistId) {
        return ArtistFavorite.builder()
                .user(user)
                .artistId(artistId)
                .createdAt(LocalDateTime.now())
                .build();
    }
}
