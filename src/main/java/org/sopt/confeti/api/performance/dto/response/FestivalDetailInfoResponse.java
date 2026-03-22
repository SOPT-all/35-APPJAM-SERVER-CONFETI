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
    String reserveAt,
    String time,
    String ageRating,
    String price,
    boolean isFavorite,
    String address,
    List<FestivalReservationResponse> reservations,
    TimetableSupportStatus timetableSupportStatus
) {

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
            DateConvertor.convertToDefaultFormat(festival.reserveAt()),
            festival.time(),
            festival.ageRating(),
            festival.price(),
            isFavorite,
            festival.address(),
            festival.reservations().stream()
                .map(reservation -> FestivalReservationResponse.of(reservation, s3FileHandler))
                .toList(),
            festival.timetableSupportStatus()
        );
    }
}
