package org.sopt.confeti.domain.artistfavorite;

import jakarta.persistence.*;

@Entity
@Table(name="artist_favorites")
public class ArtistFavorite {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long artist_favorite_id;

    @Column
    private Long user_id;

    @Column(length = 100)
    private String artist_id;

    public Long getArtist_favorite_id() {
        return artist_favorite_id;
    }

    public Long getUser_id() {
        return user_id;
    }

    public String getArtist_id() {
        return artist_id;
    }
}
