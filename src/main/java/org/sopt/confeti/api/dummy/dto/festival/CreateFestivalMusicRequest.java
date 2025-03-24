package org.sopt.confeti.api.dummy.dto.festival;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class CreateFestivalMusicRequest {

    @NotBlank
    private String musicId;
}
