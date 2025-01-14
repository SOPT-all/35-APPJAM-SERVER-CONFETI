package org.sopt.confeti.domain.concertartist;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.sopt.confeti.domain.concert.Concert;

@Entity
@Table(name="concert_artists")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
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

    @Builder
    private ConcertArtist(Concert concert, String artistId) {
        this.concert = concert;
        this.artistId = artistId;
    }

    public static ConcertArtist create(Concert concert, String artistId) {
        return ConcertArtist.builder()
                .concert(concert)
                .artistId(artistId)
                .build();
    }

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
