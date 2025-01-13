package org.sopt.confeti.domain.concertartist;

import jakarta.persistence.*;

@Entity
@Table(name="concert_artists")
public class ConcertArtist {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long concert_artist_id;

    @Column
    private Long concert_id;

    @Column(length=100)
    private String artist_id;

    public Long getConcert_artist_id() {
        return concert_artist_id;
    }

    public Long getConcert_id() {
        return concert_id;
    }

    public String getArtist_id() {
        return artist_id;
    }
}
