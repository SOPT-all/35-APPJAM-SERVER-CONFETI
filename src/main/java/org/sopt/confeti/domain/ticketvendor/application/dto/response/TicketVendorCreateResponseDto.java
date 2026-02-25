package org.sopt.confeti.domain.ticketvendor.application.dto.response;

import org.sopt.confeti.domain.ticketvendor.TicketVendor;

public record TicketVendorCreateResponseDto(
    Long id,
    String name,
    String logoPath
) {
    public static TicketVendorCreateResponseDto of(TicketVendor ticketVendor) {
        return new TicketVendorCreateResponseDto(
            ticketVendor.getId(),
            ticketVendor.getName(),
            ticketVendor.getLogoPath()
        );
    }
}
