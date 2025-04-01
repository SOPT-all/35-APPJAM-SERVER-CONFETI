package org.sopt.confeti.api.dummy.dto.festival;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateFestivalReservationUrlRequest {

    @NotBlank
    private String reservationUrl;
    @NotBlank
    private String name;
}
