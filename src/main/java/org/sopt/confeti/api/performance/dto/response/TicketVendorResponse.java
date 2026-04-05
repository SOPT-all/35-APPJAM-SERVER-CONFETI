package org.sopt.confeti.api.performance.dto.response;

import lombok.Builder;
import org.sopt.confeti.domain.ticketvendor.application.dto.response.TicketVendorDto;

@Builder
public record TicketVendorResponse(
    long ticketVendorId,
    String name,
    String logoUrl
) {

    public static TicketVendorResponse from(TicketVendorDto ticketVendorDto) {
        return TicketVendorResponse.builder()
            .ticketVendorId(ticketVendorDto.id())
            .name(ticketVendorDto.name())
            .logoUrl(ticketVendorDto.logoPath())
            .build();
    }
}
