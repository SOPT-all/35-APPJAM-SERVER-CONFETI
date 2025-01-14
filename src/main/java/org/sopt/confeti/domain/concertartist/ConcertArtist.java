package org.sopt.confeti.domain.concertartist;

import jakarta.persistence.*;
import org.sopt.confeti.domain.concert.Concert;

@Entity
@Table(name="concert_artists")
public class ConcertArtist {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="concert_artist_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "concert_id")
    private Concert concert;

    @Column(length=100, nullable = false)
    private String artistId;

    public Long getId() {
        return id;
    }

    public Concert getConcert() {
        return concert;
    }

    public String getArtistId() {
        return artistId;
    }
}
