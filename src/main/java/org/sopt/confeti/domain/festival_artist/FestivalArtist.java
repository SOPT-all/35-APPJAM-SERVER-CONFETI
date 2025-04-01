package org.sopt.confeti.domain.festival_artist;

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
import org.sopt.confeti.api.dummy.facade.dto.festival.request.CreateFestivalArtistDTO;
import org.sopt.confeti.domain.festival_time.FestivalTime;
import org.sopt.confeti.global.resolver.music_api.artist.vo.ConfetiArtist;

@Entity
@Table(name = "festival_artists")
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
    @JoinColumn(name = "festival_time_id", nullable = false)
    private FestivalTime festivalTime;

    @Builder
    public FestivalArtist(ConfetiArtist artist) {
        this.artist = artist;
    }

    public static FestivalArtist create(CreateFestivalArtistDTO festivalArtistDTO) {
        return FestivalArtist.builder()
                .artist(
                        ConfetiArtist.from(festivalArtistDTO.artistId())
                )
                .build();
    }
}
