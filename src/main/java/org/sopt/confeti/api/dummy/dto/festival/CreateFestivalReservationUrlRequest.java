package org.sopt.confeti.api.dummy.dto.festival;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

public record CreateFestivalReservationUrlRequest(
    @NotBlank String reservationUrl,
    @NotBlank String name
) {
    public CreateFestivalReservationUrlRequest() {
        this(null, null);
    }
}
