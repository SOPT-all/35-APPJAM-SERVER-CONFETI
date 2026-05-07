package org.sopt.confeti.domain.ticketvendor.application.dto.response;

import java.util.List;

public record TicketVendorInfos(
    List<TicketVendorInfo> ticketVendors
) {
    public static TicketVendorInfos from(List<TicketVendorInfo> ticketVendors) {
        return new TicketVendorInfos(ticketVendors);
    }
}
