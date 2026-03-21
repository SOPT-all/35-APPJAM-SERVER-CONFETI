package org.sopt.confeti.api.admin.facade.dto.request;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import org.sopt.confeti.api.admin.dto.request.PutAdminConcertRequest;

public record AdminConcertCommand(
    Long concertId,
    String title,
    LocalDate startAt,
    LocalDate endAt,
    String area,
    LocalDateTime reserveAt,
    String ageRating,
    String time,
    String price,
    String address,
    List<String> artistIds,
    List<ReservationUrl> reservationUrls
) {

    public static AdminConcertCommand from(PutAdminConcertRequest request) {
        return new AdminConcertCommand(
            request.concertId(), request.title(),
            request.startAt(), request.endAt(), request.area(),
            request.reserveAt(), request.ageRating(), request.time(),
            request.price(), request.address(), request.artistIds(),
            request.reservationUrls().stream().map(ReservationUrl::from).toList()
        );
    }

    public record ReservationUrl(Long ticketVendorId, String reservationUrl) {

        public static ReservationUrl from(PutAdminConcertRequest.ReservationUrlRequest req) {
            return new ReservationUrl(req.ticketVendorId(), req.reservationUrl());
        }
    }
}
