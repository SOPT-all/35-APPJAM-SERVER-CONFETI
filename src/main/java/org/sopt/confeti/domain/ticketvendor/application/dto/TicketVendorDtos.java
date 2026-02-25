package org.sopt.confeti.domain.ticketvendor.application.dto;

import java.util.List;

public record TicketVendorDtos(
    List<TicketVendorDto> ticketVendors
) {
    public static TicketVendorDtos from(final List<TicketVendorDto> ticketVendors) {
        return new TicketVendorDtos(ticketVendors);
    }
}
