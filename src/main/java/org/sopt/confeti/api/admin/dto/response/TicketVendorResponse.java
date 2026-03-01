package org.sopt.confeti.api.admin.dto.response;

import org.sopt.confeti.domain.ticketvendor.TicketVendor;
import org.sopt.confeti.domain.ticketvendor.application.dto.response.TicketVendorCreateResponseDto;
import org.sopt.confeti.domain.ticketvendor.application.dto.response.TicketVendorDto;
import org.sopt.confeti.domain.ticketvendor.application.dto.response.TicketVendorUpdateResponseDto;
import org.sopt.confeti.global.common.constant.FolderPath;
import org.sopt.confeti.global.util.S3FileHandler;

public record TicketVendorResponse(
    Long id,
    String name,
    String logoPath
) {

    public static TicketVendorResponse of(TicketVendor ticketVendor, S3FileHandler s3FileHandler) {
        return new TicketVendorResponse(
            ticketVendor.getId(),
            ticketVendor.getName(),
            s3FileHandler.getFileSignedUrl(
                FolderPath.combine(FolderPath.TICKET_VENDOR, FolderPath.LOGO),
                ticketVendor.getLogoPath()).toString()
        );
    }

    public static TicketVendorResponse of(TicketVendorDto dto, S3FileHandler s3FileHandler) {
        return new TicketVendorResponse(dto.id(), dto.name(),
            s3FileHandler.getFileUrl(FolderPath.combine(FolderPath.TICKET_VENDOR, FolderPath.LOGO),
                dto.logoPath()).toString());
    }

    public static TicketVendorResponse from(TicketVendorCreateResponseDto dto,
        S3FileHandler s3FileHandler) {
        return new TicketVendorResponse(dto.id(), dto.name(),
            s3FileHandler.getFileUrl(FolderPath.combine(FolderPath.TICKET_VENDOR, FolderPath.LOGO),
                dto.logoPath()).toString());
    }

    public static TicketVendorResponse from(TicketVendorUpdateResponseDto dto,
        S3FileHandler s3FileHandler) {
        return new TicketVendorResponse(dto.id(), dto.name(),
            s3FileHandler.getFileUrl(FolderPath.combine(FolderPath.TICKET_VENDOR, FolderPath.LOGO),
                dto.logoPath()).toString());
    }
}
