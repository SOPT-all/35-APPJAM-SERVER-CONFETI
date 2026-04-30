package org.sopt.confeti.domain.ticketvendor.application.dto.response;

import lombok.Builder;
import org.sopt.confeti.domain.ticketvendor.TicketVendor;
import org.sopt.confeti.domain.ticketvendor.TicketVendorFileInfo;

@Builder(toBuilder = true)
public record TicketVendorDto(
    Long id,
    String name,
    String logoPath,
    String logoUrl
) {

    public static TicketVendorDto from(TicketVendor ticketVendor) {
        return TicketVendorDto.builder()
            .id(ticketVendor.getId())
            .name(ticketVendor.getName())
            .logoPath(ticketVendor.getLogoPath())
            .build();
    }

    public TicketVendorDto withFileUrls(TicketVendorFileInfo fileInfo) {
        return this.toBuilder()
            .logoUrl(fileInfo.logoUrl())
            .build();
    }
}
