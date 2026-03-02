package org.sopt.confeti.api.admin.dto.response;

public record PutAdminConcertResponse(long concertId) {

    public static PutAdminConcertResponse from(long concertId) {
        return new PutAdminConcertResponse(concertId);
    }
}
