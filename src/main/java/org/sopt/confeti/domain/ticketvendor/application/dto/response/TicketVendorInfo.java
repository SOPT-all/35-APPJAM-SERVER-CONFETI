package org.sopt.confeti.domain.ticketvendor.application.dto.response;

import org.sopt.confeti.domain.ticketvendor.TicketVendorFileInfo;

public record TicketVendorInfo(
    Long id,
    String name,
    String logoUrl
) {
    public static TicketVendorInfo of(TicketVendorDto dto, TicketVendorFileInfo fileInfo) {
        return new TicketVendorInfo(
            dto.id(),
            dto.name(),
            fileInfo.logoUrl()
        );
    }
}
