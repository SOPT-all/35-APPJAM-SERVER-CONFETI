package org.sopt.confeti.domain.ticketvendor.application.dto;

public record TicketVendorCreateDto(
    String name,
    String logoPath
) {
    public static TicketVendorCreateDto of(String name, String logoPath) {
        return new TicketVendorCreateDto(name, logoPath);
    }
}
