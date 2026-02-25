package org.sopt.confeti.api.admin.dto.request;

import jakarta.validation.constraints.NotBlank;

public record UpdateTicketVendorRequest(
    @NotBlank String name,
    @NotBlank String logoPath
) {
}
