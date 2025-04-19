package org.sopt.confeti.api.user.dto.request;

public record PatchUserInfoRequest(
        String profileUrl,
        String name
) {
    public static PatchUserInfoRequest from(PatchUserInfoRequest request) {
        return new PatchUserInfoRequest(request.profileUrl(), request.name());
    }
}