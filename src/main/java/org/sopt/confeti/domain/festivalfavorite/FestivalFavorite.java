package org.sopt.confeti.domain.festivalfavorite;

import jakarta.persistence.*;

@Entity
@Table(name="festival_favorites")
public class FestivalFavorite {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long festival_favorite_id;

    @Column
    private Long user_id;

    @Column
    private Long festival_id;

    public Long getFestival_favorite_id() {
        return festival_favorite_id;
    }

    public void setFestival_favorite_id(Long festival_favorite_id) {
        this.festival_favorite_id = festival_favorite_id;
    }

    public Long getUser_id() {
        return user_id;
    }
}
