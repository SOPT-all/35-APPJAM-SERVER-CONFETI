package org.sopt.confeti.domain.ticketvendor.application.dto.request;

public record TicketVendorCreateDto(
    String name,
    String logoPath
) {
    public static TicketVendorCreateDto of(String name, String logoPath) {
        return new TicketVendorCreateDto(name, logoPath);
    }

    public static TicketVendorCreateDto from(org.sopt.confeti.api.admin.dto.request.CreateTicketVendorRequest request, String logoPath) {
        return new TicketVendorCreateDto(request.name(), logoPath);
    }

    public org.sopt.confeti.domain.ticketvendor.TicketVendor toEntity() {
        return org.sopt.confeti.domain.ticketvendor.TicketVendor.create(this.name, this.logoPath);
    }
}
