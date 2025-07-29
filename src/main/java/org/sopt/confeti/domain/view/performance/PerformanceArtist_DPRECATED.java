package org.sopt.confeti.domain.view.performance;

import jakarta.persistence.Column;
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
import org.sopt.confeti.api.dummy.facade.dto.festival.request.CreateFestivalArtistDTO;

@Entity
@Table(name = "performance_artists")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PerformanceArtist_DPRECATED {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 50, nullable = false)
    private String artistId;

    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "performance_id", nullable = false)
    private Performance_DPRECATED performanceDPRECATED;

    @Builder
    private PerformanceArtist_DPRECATED(String artistId) {
        this.artistId = artistId;
    }

    public static PerformanceArtist_DPRECATED create(CreateFestivalArtistDTO festivalArtistDTO) {
        return new PerformanceArtist_DPRECATED(festivalArtistDTO.artistId());
    }

    public static PerformanceArtist_DPRECATED create(CreateConcertArtistDTO concertArtistDTO) {
        return new PerformanceArtist_DPRECATED(concertArtistDTO.artistId());
    }
}
