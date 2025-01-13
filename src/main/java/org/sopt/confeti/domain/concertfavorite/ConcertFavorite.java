package org.sopt.confeti.domain.concertfavorite;

import jakarta.persistence.*;

@Entity
@Table(name="concert_favorites")
public class ConcertFavorite {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long concert_favorite_id;

    @Column
    private Long user_id;

    @Column
    private Long concert_id;

    public Long getConcert_favorite_id() {
        return concert_favorite_id;
    }

    public Long getUser_id() {
        return user_id;
    }

    public Long getConcert_id() {
        return concert_id;
    }
}
