package org.sopt.confeti.domain.ticketvendor.application.dto;

import org.sopt.confeti.domain.ticketvendor.TicketVendor;

public record TicketVendorDto(
    Long id,
    String name,
    String logoPath
) {
    public static TicketVendorDto of(TicketVendor ticketVendor) {
        return new TicketVendorDto(
            ticketVendor.getId(),
            ticketVendor.getName(),
            ticketVendor.getLogoPath()
        );
    }
}
