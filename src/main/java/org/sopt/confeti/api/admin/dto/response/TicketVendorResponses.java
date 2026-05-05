package org.sopt.confeti.api.admin.dto.response;

import java.util.List;
import org.sopt.confeti.domain.ticketvendor.application.dto.response.TicketVendorInfos;

public record TicketVendorResponses(
    List<TicketVendorResponse> ticketVendors
) {
    public static TicketVendorResponses from(TicketVendorInfos infos) {
        return new TicketVendorResponses(
            infos.ticketVendors().stream()
                .map(TicketVendorResponse::from)
                .toList()
        );
    }
}
