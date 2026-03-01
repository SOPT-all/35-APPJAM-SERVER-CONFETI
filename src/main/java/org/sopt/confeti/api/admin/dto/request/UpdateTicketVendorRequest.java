package org.sopt.confeti.api.admin.dto.request;

import org.springframework.web.multipart.MultipartFile;

public record UpdateTicketVendorRequest(
    String name,
    MultipartFile logoImage
) {
}
