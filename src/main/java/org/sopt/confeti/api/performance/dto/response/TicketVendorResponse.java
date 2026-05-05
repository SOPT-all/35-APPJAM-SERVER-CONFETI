package org.sopt.confeti.api.performance.dto.response;

import lombok.Builder;
import org.sopt.confeti.domain.ticketvendor.application.dto.response.TicketVendorInfo;

@Builder
public record TicketVendorResponse(
    long ticketVendorId,
    String name,
    String logoUrl
) {

    public static TicketVendorResponse from(TicketVendorInfo info) {
        return TicketVendorResponse.builder()
            .ticketVendorId(info.id())
            .name(info.name())
            .logoUrl(info.logoUrl())
            .build();
    }
}
