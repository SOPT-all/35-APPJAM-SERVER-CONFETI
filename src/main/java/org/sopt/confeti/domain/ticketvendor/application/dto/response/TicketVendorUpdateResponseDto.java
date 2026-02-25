package org.sopt.confeti.domain.ticketvendor.application.dto.response;

import org.sopt.confeti.domain.ticketvendor.TicketVendor;

public record TicketVendorUpdateResponseDto(
    Long id,
    String name,
    String logoPath
) {
    public static TicketVendorUpdateResponseDto of(TicketVendor ticketVendor) {
        return new TicketVendorUpdateResponseDto(
            ticketVendor.getId(),
            ticketVendor.getName(),
            ticketVendor.getLogoPath()
        );
    }
}
