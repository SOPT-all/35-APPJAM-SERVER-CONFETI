package org.sopt.confeti.domain.concertfavorite;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.sopt.confeti.domain.concert.Concert;
import org.sopt.confeti.domain.user.User;

@Entity
@Table(name="concert_favorites")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ConcertFavorite {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "concert_favorite_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="concert_id")
    private Concert concert;

    public Long getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public Concert getConcert() {
        return concert;
    }

    @Builder
    public ConcertFavorite(User user, Concert concert) {
        this.user = user;
        this.concert = concert;
    }

    public static ConcertFavorite create(User user, Concert concert) {
        return ConcertFavorite.builder()
                .user(user)
                .concert(concert)
                .build();
    }
}
