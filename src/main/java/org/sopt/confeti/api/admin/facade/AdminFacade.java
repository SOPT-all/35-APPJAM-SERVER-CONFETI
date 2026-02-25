package org.sopt.confeti.api.admin.facade;

import lombok.RequiredArgsConstructor;
import org.sopt.confeti.api.admin.dto.request.CreateTicketVendorRequest;
import org.sopt.confeti.api.admin.dto.request.UpdateTicketVendorRequest;
import org.sopt.confeti.api.admin.dto.response.TicketVendorResponse;
import org.sopt.confeti.domain.ticketvendor.application.TicketVendorService;
import org.sopt.confeti.domain.ticketvendor.application.dto.TicketVendorCreateDto;
import org.sopt.confeti.domain.ticketvendor.application.dto.TicketVendorCreateResponseDto;
import org.sopt.confeti.domain.ticketvendor.application.dto.TicketVendorUpdateDto;
import org.sopt.confeti.domain.ticketvendor.application.dto.TicketVendorDto;
import org.sopt.confeti.domain.ticketvendor.application.dto.TicketVendorDtos;
import org.sopt.confeti.domain.ticketvendor.application.dto.TicketVendorUpdateResponseDto;
import org.sopt.confeti.global.annotation.Facade;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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

    @Transactional
    public TicketVendorResponse updateTicketVendor(Long ticketVendorId, UpdateTicketVendorRequest request) {
        TicketVendorUpdateDto dto = TicketVendorUpdateDto.of(ticketVendorId, request.name(), request.logoPath());
        TicketVendorUpdateResponseDto responseDto = ticketVendorService.update(dto);
        
        return new TicketVendorResponse(
            responseDto.id(), 
            responseDto.name(), 
            responseDto.logoPath()
        );
    }

    @Transactional
    public void deleteTicketVendor(Long ticketVendorId) {
        ticketVendorService.delete(ticketVendorId);
    }

    @Transactional(readOnly = true)
    public TicketVendorDtos getTicketVendors() {
        return ticketVendorService.findAll();
    }
}
