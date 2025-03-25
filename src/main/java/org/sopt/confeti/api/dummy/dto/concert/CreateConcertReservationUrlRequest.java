package org.sopt.confeti.api.dummy.dto.concert;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class CreateConcertReservationUrlRequest {

    @NotBlank
    private String reservationUrl;
    @NotBlank
    private String name;
}
