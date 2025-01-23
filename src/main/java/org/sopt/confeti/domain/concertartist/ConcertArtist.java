package org.sopt.confeti.domain.concertartist;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.sopt.confeti.api.performance.facade.dto.request.CreateConcertArtistDTO;
import org.sopt.confeti.domain.concert.Concert;
import org.sopt.confeti.global.util.artistsearcher.ConfetiArtist;

@Entity
@Table(name="concert_artists")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ConcertArtist {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="concert_artist_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "concert_id")
    private Concert concert;

    @Embedded
    private ConfetiArtist artist;

    public void setConcert(Concert concert) {
        this.concert = concert;
    }

    @Builder
    public ConcertArtist(ConfetiArtist artist) {
        this.artist = artist;
    }

    public static ConcertArtist create(final CreateConcertArtistDTO concertArtistDTO) {
        return ConcertArtist.builder()
                .artist(
                        ConfetiArtist.from(concertArtistDTO.artistId())
                )
                .build();
    }
}
