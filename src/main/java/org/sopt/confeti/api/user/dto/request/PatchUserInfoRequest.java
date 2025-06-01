package org.sopt.confeti.api.user.dto.request;

import org.springframework.web.multipart.MultipartFile;

public record PatchUserInfoRequest(
        String name,
        MultipartFile profileFile
        ) {
    public static PatchUserInfoRequest from(PatchUserInfoRequest request) {
        return new PatchUserInfoRequest(request.name(), request.profileFile());
    }
}