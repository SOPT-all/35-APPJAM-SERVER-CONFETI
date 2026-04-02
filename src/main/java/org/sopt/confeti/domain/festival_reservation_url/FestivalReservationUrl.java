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
import org.sopt.confeti.domain.festival.Festival;
import org.sopt.confeti.domain.ticketvendor.TicketVendor;

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

    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ticket_vendor_id")
    private TicketVendor ticketVendor;

    @Column(length = 500, nullable = false)
    private String reservationUrl;

    @Builder
    public FestivalReservationUrl(String reservationUrl, TicketVendor ticketVendor) {
        this.reservationUrl = reservationUrl;
        this.ticketVendor = ticketVendor;
    }

    public static FestivalReservationUrl create(String reservationUrl, TicketVendor ticketVendor) {
        return FestivalReservationUrl.builder()
            .reservationUrl(reservationUrl)
            .ticketVendor(ticketVendor)
            .build();
    }

    public void updateReservationUrl(String reservationUrl) {
        this.reservationUrl = reservationUrl;
    }
}
