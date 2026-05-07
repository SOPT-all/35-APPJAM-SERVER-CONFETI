package org.sopt.confeti.domain.concert_reservation_url.application;

import lombok.RequiredArgsConstructor;
import org.sopt.confeti.domain.concert_reservation_url.ConcertReservationFileInfo;
import org.sopt.confeti.domain.concert_reservation_url.ConcertReservationUrl;
import org.sopt.confeti.domain.ticketvendor.application.TicketVendorFileService;
import org.sopt.confeti.domain.ticketvendor.application.dto.response.TicketVendorDto;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ConcertReservationFileService {

    private final TicketVendorFileService ticketVendorFileService;

    public ConcertReservationFileInfo getFileInfo(ConcertReservationUrl reservationUrl) {
        TicketVendorDto vendorDto = TicketVendorDto.from(reservationUrl.getTicketVendor());
        return ConcertReservationFileInfo.builder()
            .ticketVendorFileInfo(ticketVendorFileService.getFileInfo(vendorDto))
            .build();
    }
}
