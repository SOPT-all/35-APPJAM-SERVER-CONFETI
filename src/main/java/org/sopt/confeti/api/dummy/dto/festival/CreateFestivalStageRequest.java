package org.sopt.confeti.api.dummy.dto.festival;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateFestivalStageRequest {

    @NotBlank
    private String name;
    @Min(0)
    @Max(10)
    private int order;

    @Valid
    @Size(min = 1)
    private List<CreateFestivalTimeRequest> times = new ArrayList<>();
}
