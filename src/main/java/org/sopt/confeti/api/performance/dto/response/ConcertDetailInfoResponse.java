package org.sopt.confeti.api.performance.dto.response;

import java.time.LocalDate;
import java.time.LocalDateTime;
import org.sopt.confeti.api.performance.facade.dto.response.ConcertDetailDTO;
import org.sopt.confeti.global.common.constant.FolderPath;
import org.sopt.confeti.global.util.DateConvertor;
import org.sopt.confeti.global.util.S3FileHandler;

public record ConcertDetailInfoResponse(
        long concertId,
        String posterUrl,
        String posterBgUrl,
        String title,
        String subtitle,
        String startAt,
        String endAt,
        String area,
        String reserveAt,
        String reservationUrl,
        String time,
        String ageRating,
        String reservationOffice,
        String price,
        String infoImgUrl,
        boolean isFavorite
) {
    public static ConcertDetailInfoResponse of(final ConcertDetailDTO concertDetailDTO, final S3FileHandler s3FileHandler) {
        return new ConcertDetailInfoResponse(
                concertDetailDTO.concertId(),
                s3FileHandler.getFileUrl(FolderPath.combine(FolderPath.CONCERT, FolderPath.POSTER), concertDetailDTO.posterPath()).getPath(),
                s3FileHandler.getFileUrl(FolderPath.combine(FolderPath.CONCERT, FolderPath.POSTER_BG), concertDetailDTO.posterBgPath()).getPath(),
                concertDetailDTO.title(),
                concertDetailDTO.subtitle(),
                DateConvertor.convertToLocalDate(concertDetailDTO.startAt()),
                DateConvertor.convertToLocalDate(concertDetailDTO.endAt()),
                concertDetailDTO.area(),
                DateConvertor.convert(concertDetailDTO.reserveAt()),
                concertDetailDTO.reservationUrl(),
                concertDetailDTO.time(),
                concertDetailDTO.ageRating(),
                concertDetailDTO.reservationOffice(),
                concertDetailDTO.price(),
                s3FileHandler.getFileUrl(FolderPath.combine(FolderPath.CONCERT, FolderPath.DETAIL), concertDetailDTO.infoImgPath()).getPath(),
                concertDetailDTO.isFavorite()
        );
    }
}
