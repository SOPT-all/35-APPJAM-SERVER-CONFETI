package org.sopt.confeti.domain.ticketvendor.application.dto.request;

import org.sopt.confeti.api.admin.dto.request.CreateTicketVendorRequest;
import org.sopt.confeti.domain.ticketvendor.TicketVendor;

public record TicketVendorCreateDto(
    String name,
    String logoPath
) {
    public static TicketVendorCreateDto of(String name, String logoPath) {
        return new TicketVendorCreateDto(name, logoPath);
    }

    public static TicketVendorCreateDto from(CreateTicketVendorRequest request, String logoPath) {
        return new TicketVendorCreateDto(request.name(), logoPath);
    }

    public TicketVendor toEntity() {
        return TicketVendor.create(this.name, this.logoPath);
    }
}
