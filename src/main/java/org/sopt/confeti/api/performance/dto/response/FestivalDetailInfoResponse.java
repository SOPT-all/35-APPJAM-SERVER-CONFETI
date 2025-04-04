package org.sopt.confeti.api.performance.dto.response;

import java.util.List;
import org.sopt.confeti.api.performance.facade.dto.response.FestivalDetailDTO;
import org.sopt.confeti.global.common.constant.FolderPath;
import org.sopt.confeti.global.util.DateConvertor;
import org.sopt.confeti.global.util.S3FileHandler;

public record FestivalDetailInfoResponse(
        long festivalId,
        String posterUrl,
        String posterBgUrl,
        String title,
        String subtitle,
        String startAt,
        String endAt,
        String area,
        String reserveAt,
        String time,
        String ageRating,
        String price,
        boolean isFavorite,
        String address,
        List<FestivalReservationResponse> reservations
) {
    public static FestivalDetailInfoResponse of(final FestivalDetailDTO festival, final S3FileHandler s3FileHandler) {
        return new FestivalDetailInfoResponse(
                festival.festivalId(),
                s3FileHandler.getFileUrl(FolderPath.combine(FolderPath.FESTIVAL, FolderPath.POSTER),
                        festival.posterPath()).toString(),
                s3FileHandler.getFileUrl(FolderPath.combine(FolderPath.FESTIVAL, FolderPath.POSTER_BG),
                        festival.posterBgPath()).toString(),
                festival.title(),
                festival.subtitle(),
                DateConvertor.convertToDefaultFormat(festival.startAt()),
                DateConvertor.convertToDefaultFormat(festival.endAt()),
                festival.area(),
                DateConvertor.convertToDefaultFormat(festival.reserveAt()),
                festival.time(),
                festival.ageRating(),
                festival.price(),
                festival.isFavorite(),
                festival.address(),
                festival.reservations().stream()
                        .map(reservation -> FestivalReservationResponse.of(reservation, s3FileHandler))
                        .toList()
        );
    }
}
