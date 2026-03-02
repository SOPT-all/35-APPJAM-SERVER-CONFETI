package org.sopt.confeti.api.admin.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.sopt.confeti.global.exception.BadRequestException;
import org.sopt.confeti.global.message.ErrorMessage;

@Slf4j
public record PutAdminConcertRequest(
    Long concertId,
    @NotBlank String title,
    @NotBlank String subtitle,
    @NotNull LocalDate startAt,
    @NotNull LocalDate endAt,
    @NotBlank String area,
    @NotNull LocalDateTime reserveAt,
    @NotBlank String ageRating,
    @NotBlank String time,
    @NotBlank String price,
    @NotBlank String address,
    @NotEmpty List<@NotBlank String> artistIds,
    @NotNull @Valid List<ReservationUrlRequest> reservationUrls
) {

    public void validate() {
        validateDuration();
        validateReserveDate();
    }

    private void validateDuration() {
        if (startAt.isAfter(endAt)) {
            log.warn("PutAdminConcertRequest.validateDuration : 시작 날짜는 끝 날짜보다 작거나 같아야 합니다.");
            throw new BadRequestException(ErrorMessage.BAD_REQUEST);
        }
    }

    private void validateReserveDate() {
        if (!reserveAt.isBefore(startAt.atStartOfDay())) {
            log.warn("PutAdminConcertRequest.validateReserveDate : 예약 일자는 시작 날짜보다 작아야 합니다.");
            throw new BadRequestException(ErrorMessage.BAD_REQUEST);
        }
    }

    public record ReservationUrlRequest(
        @NotNull Long ticketVendorId,
        @NotBlank String reservationUrl
    ) {

    }
}
