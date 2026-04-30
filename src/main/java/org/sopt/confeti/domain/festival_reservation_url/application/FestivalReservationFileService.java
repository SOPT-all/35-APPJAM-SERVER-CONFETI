package org.sopt.confeti.domain.festival_reservation_url.application;

import lombok.RequiredArgsConstructor;
import org.sopt.confeti.domain.festival_reservation_url.FestivalReservationFileInfo;
import org.sopt.confeti.domain.festival_reservation_url.FestivalReservationUrl;
import org.sopt.confeti.domain.ticketvendor.application.TicketVendorFileService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FestivalReservationFileService {

    private final TicketVendorFileService ticketVendorFileService;

    public FestivalReservationFileInfo getFileUrls(FestivalReservationUrl reservationUrl) {
        return FestivalReservationFileInfo.builder()
            .ticketVendorFileInfo(
                ticketVendorFileService.getFileUrls(reservationUrl.getTicketVendor()))
            .build();
    }
}
