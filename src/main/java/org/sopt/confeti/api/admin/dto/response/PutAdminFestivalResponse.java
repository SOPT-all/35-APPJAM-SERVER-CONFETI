package org.sopt.confeti.api.admin.dto.response;

public record PutAdminFestivalResponse(long festivalId) {

    public static PutAdminFestivalResponse from(long festivalId) {
        return new PutAdminFestivalResponse(festivalId);
    }
}
