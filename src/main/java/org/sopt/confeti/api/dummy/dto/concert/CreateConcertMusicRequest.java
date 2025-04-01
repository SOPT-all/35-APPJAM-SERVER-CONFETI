package org.sopt.confeti.api.dummy.dto.concert;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateConcertMusicRequest {

    @NotBlank
    private String musicId;
}
