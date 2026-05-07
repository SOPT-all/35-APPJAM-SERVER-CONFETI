package org.sopt.confeti.domain.concert_reservation_url;

import lombok.Builder;
import org.sopt.confeti.domain.ticketvendor.TicketVendorFileInfo;

@Builder
public record ConcertReservationFileInfo(
    TicketVendorFileInfo ticketVendorFileInfo
) {

}
