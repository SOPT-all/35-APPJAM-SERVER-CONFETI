package org.sopt.confeti.api.admin.dto.response;

import org.sopt.confeti.domain.ticketvendor.application.dto.response.TicketVendorInfo;

public record TicketVendorResponse(
    Long id,
    String name,
    String logoUrl
) {

    public static TicketVendorResponse from(TicketVendorInfo info) {
        return new TicketVendorResponse(
            info.id(),
            info.name(),
            info.logoUrl()
        );
    }
}
