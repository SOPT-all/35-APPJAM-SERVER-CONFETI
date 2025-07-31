package org.sopt.confeti.domain.concert_reservation_url;

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
import org.sopt.confeti.api.dummy.facade.dto.concert.request.CreateConcertReservationUrlDTO;
import org.sopt.confeti.domain.concert.Concert;

@Entity
@Table(name = "concert_reservation_urls")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ConcertReservationUrl {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "concert_id")
    private Concert concert;

    @Column(length = 200, nullable = false)
    private String reservationUrl;

    @Column(length = 50, nullable = false)
    private String name;

    @Column(length = 250, nullable = false)
    private String logoPath;

    @Builder
    public ConcertReservationUrl(String reservationUrl, String name, String logoPath) {
        this.reservationUrl = reservationUrl;
        this.name = name;
        this.logoPath = logoPath;
    }

    public static ConcertReservationUrl create(CreateConcertReservationUrlDTO concertReservationUrlDTO) {
        return ConcertReservationUrl.builder()
                .reservationUrl(concertReservationUrlDTO.reservationUrl())
                .name(concertReservationUrlDTO.name())
                .logoPath(concertReservationUrlDTO.logoPath())
                .build();
    }
}
