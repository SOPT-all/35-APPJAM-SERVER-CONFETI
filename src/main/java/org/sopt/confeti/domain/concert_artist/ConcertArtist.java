package org.sopt.confeti.domain.concert_artist;

import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.sopt.confeti.api.dummy.facade.dto.concert.request.CreateConcertArtistDTO;
import org.sopt.confeti.domain.concert.Concert;
import org.sopt.confeti.global.resolver.music_api.artist.vo.ConfetiArtist;

@Entity
@Table(name = "concert_artists")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ConcertArtist {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "concert_id")
    private Concert concert;

    @Embedded
    private ConfetiArtist artist;

    @Builder
    public ConcertArtist(ConfetiArtist artist) {
        this.artist = artist;
    }

    public static ConcertArtist create(CreateConcertArtistDTO concertArtistDTO) {
        return ConcertArtist.builder()
                .artist(ConfetiArtist.from(concertArtistDTO.artistId()))
                .build();
    }
}
