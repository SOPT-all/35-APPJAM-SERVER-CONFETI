package org.sopt.confeti.api.performance.dto.response;

import java.util.List;
import org.sopt.confeti.api.performance.facade.dto.response.FestivalDetailDTO;
import org.sopt.confeti.domain.festival.infra.TimetableSupportStatus;
import org.sopt.confeti.global.common.constant.FolderPath;
import org.sopt.confeti.global.util.DateConvertor;
import org.sopt.confeti.global.util.S3FileHandler;

public record FestivalDetailInfoResponse(
    long festivalId,
    String posterUrl,
    String title,
    String startAt,
    String endAt,
    String area,
    String time,
    String ageRating,
    String price,
    boolean isFavorite,
    String address,
    List<FestivalReservationResponse> reservations,
    List<ReservationScheduleResponse> reservationSchedules,
    TimetableSupportStatus timetableSupportStatus
) {

    public record ReservationScheduleResponse(
        long reservationScheduleId,
        String roundName,
        String reserveAt
    ) {

        public static ReservationScheduleResponse from(FestivalDetailDTO.ReservationScheduleDTO dto) {
            return new ReservationScheduleResponse(
                dto.reservationScheduleId(),
                dto.roundName(),
                DateConvertor.convertToDefaultFormat(dto.reserveAt())
            );
        }
    }

    public static FestivalDetailInfoResponse of(FestivalDetailDTO festival, boolean isFavorite,
        S3FileHandler s3FileHandler) {
        return new FestivalDetailInfoResponse(
            festival.festivalId(),
            s3FileHandler.getFileUrl(FolderPath.combine(FolderPath.FESTIVAL, FolderPath.POSTER),
                festival.posterPath()).toString(),
            festival.title(),
            DateConvertor.convertToDefaultFormat(festival.startAt()),
            DateConvertor.convertToDefaultFormat(festival.endAt()),
            festival.area(),
            festival.time(),
            festival.ageRating(),
            festival.price(),
            isFavorite,
            festival.address(),
            festival.reservations().stream()
                .map(reservation -> FestivalReservationResponse.of(reservation, s3FileHandler))
                .toList(),
            festival.reservationSchedules().stream()
                .map(ReservationScheduleResponse::from)
                .toList(),
            festival.timetableSupportStatus()
        );
    }
}
