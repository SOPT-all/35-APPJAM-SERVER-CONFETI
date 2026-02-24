package org.sopt.confeti.api.dummy.dto.festival;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

public record CreateFestivalTimeRequest(
    @NotNull LocalTime startAt,
    @NotNull LocalTime endAt,
    @Valid @Size(min = 1) List<CreateFestivalArtistRequest> artists
) {
    public CreateFestivalTimeRequest() {
        this(null, null, new ArrayList<>());
    }
}
