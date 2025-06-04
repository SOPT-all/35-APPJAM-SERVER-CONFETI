package org.sopt.confeti.domain.artist_favorite;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.sopt.confeti.domain.user.User;
import org.sopt.confeti.global.resolver.music_api.artist.vo.ConfetiArtist;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Table(name = "artist_favorites")
@EntityListeners(AuditingEntityListener.class)
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
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @Embedded
    private ConfetiArtist artist;

    @Builder
    private ArtistFavorite(User user, String artistId) {
        this.user = user;
        this.artist = ConfetiArtist.from(artistId);
    }

    public static ArtistFavorite create(User user, String artistId) {
        return ArtistFavorite.builder()
                .user(user)
                .artistId(artistId)
                .build();
    }
}
