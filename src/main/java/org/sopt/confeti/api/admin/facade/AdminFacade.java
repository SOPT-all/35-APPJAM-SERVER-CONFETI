package org.sopt.confeti.api.admin.facade;

import lombok.RequiredArgsConstructor;
import org.sopt.confeti.api.admin.dto.request.CreateTicketVendorRequest;
import org.sopt.confeti.api.admin.dto.response.TicketVendorResponse;
import org.sopt.confeti.domain.ticketvendor.application.TicketVendorService;
import org.sopt.confeti.domain.ticketvendor.application.dto.TicketVendorCreateDto;
import org.sopt.confeti.domain.ticketvendor.application.dto.TicketVendorCreateResponseDto;
import org.sopt.confeti.global.annotation.Facade;
import org.springframework.transaction.annotation.Transactional;

@Facade
@RequiredArgsConstructor
public class AdminFacade {

    private final TicketVendorService ticketVendorService;

    @Transactional
    public TicketVendorResponse createTicketVendor(CreateTicketVendorRequest request) {
        TicketVendorCreateDto dto = TicketVendorCreateDto.of(request.name(), request.logoPath());
        TicketVendorCreateResponseDto responseDto = ticketVendorService.create(dto);
        
        return new TicketVendorResponse(
            responseDto.id(), 
            responseDto.name(), 
            responseDto.logoPath()
        );
    }
}
