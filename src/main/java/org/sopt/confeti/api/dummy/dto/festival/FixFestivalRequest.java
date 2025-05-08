package org.sopt.confeti.api.dummy.dto.festival;

import jakarta.validation.Valid;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FixFestivalRequest {

    @Valid
    private List<CreateFestivalDateRequest> dates;
}
