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
import org.sopt.confeti.domain.concert.Concert;
import org.sopt.confeti.domain.ticketvendor.TicketVendor;

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

    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ticket_vendor_id")
    private TicketVendor ticketVendor;

    @Column(length = 200, nullable = false)
    private String reservationUrl;

    @Builder
    public ConcertReservationUrl(String reservationUrl, TicketVendor ticketVendor) {
        this.reservationUrl = reservationUrl;
        this.ticketVendor = ticketVendor;
    }

    public static ConcertReservationUrl create(String reservationUrl, TicketVendor ticketVendor) {
        return ConcertReservationUrl.builder()
            .reservationUrl(reservationUrl)
            .ticketVendor(ticketVendor)
            .build();
    }
}
