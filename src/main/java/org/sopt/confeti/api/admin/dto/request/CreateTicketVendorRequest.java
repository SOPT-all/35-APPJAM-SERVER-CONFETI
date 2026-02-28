package org.sopt.confeti.api.admin.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.web.multipart.MultipartFile;

public record CreateTicketVendorRequest(
    @NotBlank String name,
    @NotNull MultipartFile logoImage
) {
}
