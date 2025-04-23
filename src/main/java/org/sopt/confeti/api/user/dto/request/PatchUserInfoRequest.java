package org.sopt.confeti.api.user.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
public class PatchUserInfoRequest {
    @NotBlank
    private String name;
    @NotEmpty
    private MultipartFile profileFile;
}