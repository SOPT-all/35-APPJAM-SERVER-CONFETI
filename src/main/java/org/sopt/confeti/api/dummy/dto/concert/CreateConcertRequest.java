package org.sopt.confeti.api.dummy.dto.concert;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

public record CreateConcertRequest(
    @NotBlank String title,
    @NotBlank String subtitle,
    @NotNull LocalDateTime startAt,
    @NotNull LocalDateTime endAt,
    @NotBlank String area,
    @NotNull LocalDateTime reserveAt,
    @NotBlank String ageRating,
    @NotBlank String time,
    @NotBlank String price,
    @NotBlank String address,
    @Valid @Size(min = 1) List<CreateConcertArtistRequest> artists,
    @Valid @Size(min = 1) List<CreateConcertReservationUrlRequest> reservationUrls
) {
    public CreateConcertRequest() {
        this(null, null, null, null, null, null, null, null, null, null, new ArrayList<>(), new ArrayList<>());
    }
}
