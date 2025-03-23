package org.sopt.confeti.domain.festivalartist;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.sopt.confeti.api.performance.facade.dto.request.CreateFestivalArtistDTO;
import org.sopt.confeti.domain.festivaltime.FestivalTime;

import org.sopt.confeti.global.resolver.artist.ConfetiArtist;

@Entity
@Table(name="festival_artists")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FestivalArtist {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    private ConfetiArtist artist;

    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="festival_time_id", nullable=false)
    private FestivalTime festivalTime;

    @Builder
    public FestivalArtist(ConfetiArtist artist) {
        this.artist = artist;
    }

    public static FestivalArtist create(final CreateFestivalArtistDTO createFestivalArtistDTO) {
        return FestivalArtist.builder()
                .artist(
                        ConfetiArtist.from(createFestivalArtistDTO.artistId())
                )
                .build();
    }
}
