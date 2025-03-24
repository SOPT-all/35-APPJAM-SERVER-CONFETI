package org.sopt.confeti.api.dummy.dto.festival;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class CreateFestivalDateRequest {

    @NotNull
    private LocalDateTime festivalAt;
    @NotNull
    private LocalDateTime openAt;

    @Valid
    @Size(min = 1)
    private List<CreateFestivalStageRequest> stages = new ArrayList<>();
}
