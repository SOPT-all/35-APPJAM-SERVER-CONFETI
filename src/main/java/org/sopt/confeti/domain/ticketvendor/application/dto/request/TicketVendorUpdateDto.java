package org.sopt.confeti.domain.ticketvendor.application.dto.request;

public record TicketVendorUpdateDto(
    Long ticketVendorId,
    String name,
    String logoPath
) {
    public static TicketVendorUpdateDto of(Long ticketVendorId, String name, String logoPath) {
        return new TicketVendorUpdateDto(ticketVendorId, name, logoPath);
    }
}
