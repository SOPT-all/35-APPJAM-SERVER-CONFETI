package org.sopt.confeti.domain.ticketvendor.application.dto.request;

import org.sopt.confeti.api.admin.dto.request.UpdateTicketVendorRequest;

public record TicketVendorUpdateDto(
    Long ticketVendorId,
    String name,
    String logoPath
) {
    public static TicketVendorUpdateDto of(Long ticketVendorId, String name, String logoPath) {
        return new TicketVendorUpdateDto(ticketVendorId, name, logoPath);
    }

    public static TicketVendorUpdateDto of(Long ticketVendorId, UpdateTicketVendorRequest request, String logoPath) {
        return new TicketVendorUpdateDto(ticketVendorId, request != null ? request.name() : null, logoPath);
    }
}
