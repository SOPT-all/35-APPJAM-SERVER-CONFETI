package org.sopt.confeti.api.dummy.dto.concert;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

public record CreateConcertReservationUrlRequest(
    @NotBlank String reservationUrl,
    @NotBlank String name
) {
    public CreateConcertReservationUrlRequest() {
        this(null, null);
    }
}
