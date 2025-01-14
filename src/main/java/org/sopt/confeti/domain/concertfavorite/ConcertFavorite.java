package org.sopt.confeti.domain.concertfavorite;

import jakarta.persistence.*;
import org.sopt.confeti.domain.concert.Concert;
import org.sopt.confeti.domain.user.User;

@Entity
@Table(name="concert_favorites")
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
}
