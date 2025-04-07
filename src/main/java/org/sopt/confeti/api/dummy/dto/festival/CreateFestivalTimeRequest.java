package org.sopt.confeti.api.dummy.dto.festival;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateFestivalTimeRequest {

    @NotNull
    private LocalTime startAt;
    @NotNull
    private LocalTime endAt;

    @Valid
    @Size(min = 1)
    private List<CreateFestivalArtistRequest> artists = new ArrayList<>();
}
