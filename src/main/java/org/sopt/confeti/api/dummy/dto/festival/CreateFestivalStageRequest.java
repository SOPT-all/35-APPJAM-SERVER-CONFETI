package org.sopt.confeti.api.dummy.dto.festival;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

public record CreateFestivalStageRequest(
    @NotBlank String name,
    @Min(0) @Max(10) int order,
    @Valid List<CreateFestivalTimeRequest> times
) {
    public CreateFestivalStageRequest() {
        this(null, 0, new ArrayList<>());
    }
}
