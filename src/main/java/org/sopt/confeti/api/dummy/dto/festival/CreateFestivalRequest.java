package org.sopt.confeti.api.dummy.dto.festival;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.sopt.confeti.domain.festival.infra.TimetableSupportStatus;
import lombok.Getter;
import lombok.Setter;

public record CreateFestivalRequest(
    @NotBlank String title,
    @NotBlank String subtitle,
    @NotNull LocalDate startAt,
    @NotNull LocalDate endAt,
    @NotBlank String area,
    @NotNull LocalDateTime reserveAt,
    @NotBlank String ageRating,
    @NotBlank String time,
    @NotBlank String price,
    @NotBlank String address,
    @NotNull TimetableSupportStatus timetableSupportStatus,
    @Valid @Size(min = 1) List<CreateFestivalReservationUrlRequest> reservationUrls,
    @Valid @Size(min = 1) List<CreateFestivalDateRequest> dates
) {
    public CreateFestivalRequest() {
        this(null, null, null, null, null, null, null, null, null, null, null, new ArrayList<>(), new ArrayList<>());
    }
}
