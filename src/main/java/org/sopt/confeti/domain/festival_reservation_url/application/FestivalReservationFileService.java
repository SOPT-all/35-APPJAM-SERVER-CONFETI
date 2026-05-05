package org.sopt.confeti.domain.festival_reservation_url.application;

import lombok.RequiredArgsConstructor;
import org.sopt.confeti.domain.festival_reservation_url.FestivalReservationFileInfo;
import org.sopt.confeti.domain.festival_reservation_url.FestivalReservationUrl;
import org.sopt.confeti.domain.ticketvendor.application.TicketVendorFileService;
import org.sopt.confeti.domain.ticketvendor.application.dto.response.TicketVendorDto;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FestivalReservationFileService {

    private final TicketVendorFileService ticketVendorFileService;

    public FestivalReservationFileInfo getFileInfo(FestivalReservationUrl reservationUrl) {
        TicketVendorDto vendorDto = TicketVendorDto.from(reservationUrl.getTicketVendor());
        return FestivalReservationFileInfo.builder()
            .ticketVendorFileInfo(ticketVendorFileService.getFileInfo(vendorDto))
            .build();
    }
}
