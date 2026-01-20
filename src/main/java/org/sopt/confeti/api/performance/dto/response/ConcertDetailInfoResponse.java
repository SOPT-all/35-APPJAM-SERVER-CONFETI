package org.sopt.confeti.api.performance.dto.response;

import java.util.List;
import org.sopt.confeti.api.performance.facade.dto.response.ConcertDetailDTO;
import org.sopt.confeti.global.common.constant.FolderPath;
import org.sopt.confeti.global.util.DateConvertor;
import org.sopt.confeti.global.util.S3FileHandler;

public record ConcertDetailInfoResponse(
    long concertId,
    String posterUrl,
    String title,
    String subtitle,
    String startAt,
    String endAt,
    String area,
    String reserveAt,
    String time,
    String ageRating,
    String price,
    String address,
    boolean isFavorite,
    List<ConcertReservationResponse> reservations
) {

    public static ConcertDetailInfoResponse of(ConcertDetailDTO concertDetailDTO,
        boolean isFavorite,
        S3FileHandler s3FileHandler) {
        return new ConcertDetailInfoResponse(
            concertDetailDTO.concertId(),
            s3FileHandler.getFileUrl(FolderPath.combine(FolderPath.CONCERT, FolderPath.POSTER),
                concertDetailDTO.posterPath()).toString(),
            concertDetailDTO.title(),
            concertDetailDTO.subtitle(),
            DateConvertor.convertToDefaultFormat(concertDetailDTO.startAt()),
            DateConvertor.convertToDefaultFormat(concertDetailDTO.endAt()),
            concertDetailDTO.area(),
            DateConvertor.convertToDefaultFormat(concertDetailDTO.reserveAt()),
            concertDetailDTO.time(),
            concertDetailDTO.ageRating(),
            concertDetailDTO.price(),
            concertDetailDTO.address(),
            isFavorite,
            concertDetailDTO.reservations().stream()
                .map(reservation -> ConcertReservationResponse.of(reservation, s3FileHandler))
                .toList()
        );
    }
}
