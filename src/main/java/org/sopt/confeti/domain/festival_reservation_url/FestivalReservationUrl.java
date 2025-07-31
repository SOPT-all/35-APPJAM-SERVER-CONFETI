package org.sopt.confeti.domain.festival_reservation_url;

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
import org.sopt.confeti.api.dummy.facade.dto.festival.request.CreateFestivalReservationUrlDTO;
import org.sopt.confeti.domain.festival.Festival;

@Entity
@Table(name = "festival_reservation_urls")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FestivalReservationUrl {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "festival_id")
    private Festival festival;

    @Column(length = 200, nullable = false)
    private String reservationUrl;

    @Column(length = 50, nullable = false)
    private String name;

    @Column(length = 250, nullable = false)
    private String logoPath;

    @Builder
    public FestivalReservationUrl(String reservationUrl, String name, String logoPath) {
        this.reservationUrl = reservationUrl;
        this.name = name;
        this.logoPath = logoPath;
    }

    public static FestivalReservationUrl create(CreateFestivalReservationUrlDTO festivalReservationUrlDTO) {
        return FestivalReservationUrl.builder()
                .reservationUrl(festivalReservationUrlDTO.reservationUrl())
                .name(festivalReservationUrlDTO.name())
                .logoPath(festivalReservationUrlDTO.logoPath())
                .build();
    }
}
