package org.sopt.confeti.api.admin.dto.response;

import org.sopt.confeti.domain.ticketvendor.application.dto.TicketVendorDto;
import org.sopt.confeti.domain.ticketvendor.TicketVendor;

public record TicketVendorResponse(
    Long id,
    String name,
    String logoPath
) {
    public static TicketVendorResponse of(TicketVendor ticketVendor) {
        return new TicketVendorResponse(
            ticketVendor.getId(),
            ticketVendor.getName(),
            ticketVendor.getLogoPath()
        );
    }

    public static TicketVendorResponse of(TicketVendorDto dto) {
        return new TicketVendorResponse(dto.id(), dto.name(), dto.logoPath());
    }
}
