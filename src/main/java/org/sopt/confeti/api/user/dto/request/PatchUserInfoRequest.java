package org.sopt.confeti.api.user.dto.request;

public record PatchUserInfoRequest(
        String name,
        String profileUrl
        ) {
    public static PatchUserInfoRequest from(PatchUserInfoRequest request) {
        return new PatchUserInfoRequest(request.name(), request.profileUrl());
    }
}