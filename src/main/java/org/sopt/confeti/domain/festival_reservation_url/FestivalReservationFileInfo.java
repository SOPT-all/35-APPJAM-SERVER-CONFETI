package org.sopt.confeti.domain.festival_reservation_url;

import lombok.Builder;
import org.sopt.confeti.domain.ticketvendor.TicketVendorFileInfo;

@Builder
public record FestivalReservationFileInfo(
    TicketVendorFileInfo ticketVendorFileInfo
) {

}
