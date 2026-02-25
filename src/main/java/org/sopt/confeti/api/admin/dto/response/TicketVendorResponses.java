package org.sopt.confeti.api.admin.dto.response;

import java.util.List;
import org.sopt.confeti.domain.ticketvendor.application.dto.response.TicketVendorDtos;

public record TicketVendorResponses(
    List<TicketVendorResponse> ticketVendors
) {
    public static TicketVendorResponses from(final TicketVendorDtos dtos) {
        return new TicketVendorResponses(
            dtos.ticketVendors().stream()
                .map(TicketVendorResponse::of)
                .toList()
        );
    }
}
