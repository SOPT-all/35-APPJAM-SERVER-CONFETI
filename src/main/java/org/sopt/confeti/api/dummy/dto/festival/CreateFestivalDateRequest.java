package org.sopt.confeti.api.dummy.dto.festival;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

public record CreateFestivalDateRequest(
    @NotNull LocalDate festivalAt,
    @NotNull LocalTime openAt,
    @Valid @Size(min = 1) List<CreateFestivalStageRequest> stages
) {
    public CreateFestivalDateRequest() {
        this(null, null, new ArrayList<>());
    }
}
