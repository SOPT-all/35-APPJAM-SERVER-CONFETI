package org.sopt.confeti.api.admin.dto.response;

import java.time.LocalDate;
import java.util.List;
import org.sopt.confeti.api.admin.facade.dto.response.AdminConcertListInfo;
import org.sopt.confeti.api.admin.facade.dto.response.AdminConcertListInfo.ConcertInfo;
import org.sopt.confeti.global.common.constant.FolderPath;
import org.sopt.confeti.global.util.S3FileHandler;

public record AdminConcertListResponse(
    UpcomingConcerts upcomingConcerts,
    FinishedConcerts finishedConcerts
) {

    public static AdminConcertListResponse of(AdminConcertListInfo info,
        S3FileHandler s3FileHandler) {
        return new AdminConcertListResponse(
            UpcomingConcerts.of(info.upcomingConcerts(), s3FileHandler),
            FinishedConcerts.of(info.finishedConcerts(), s3FileHandler)
        );
    }

    public record UpcomingConcerts(
        List<ConcertResponse> concerts,
        int count
    ) {

        public static UpcomingConcerts of(List<ConcertInfo> infos, S3FileHandler s3FileHandler) {
            List<ConcertResponse> concerts = infos.stream()
                .map(info -> ConcertResponse.of(info, s3FileHandler))
                .toList();
            return new UpcomingConcerts(concerts, concerts.size());
        }
    }

    public record FinishedConcerts(
        List<ConcertResponse> concerts,
        int count
    ) {

        public static FinishedConcerts of(List<ConcertInfo> infos, S3FileHandler s3FileHandler) {
            List<ConcertResponse> concerts = infos.stream()
                .map(info -> ConcertResponse.of(info, s3FileHandler))
                .toList();
            return new FinishedConcerts(concerts, concerts.size());
        }
    }

    public record ConcertResponse(
        long concertId,
        String posterUrl,
        String title,
        String subtitle,
        LocalDate startAt,
        LocalDate endAt,
        String area
    ) {

        public static ConcertResponse of(ConcertInfo info, S3FileHandler s3FileHandler) {
            return new ConcertResponse(
                info.concertId(),
                s3FileHandler.getFileUrl(
                    FolderPath.combine(FolderPath.CONCERT, FolderPath.POSTER),
                    info.posterPath()
                ).toString(),
                info.title(),
                info.subtitle(),
                info.startAt(),
                info.endAt(),
                info.area()
            );
        }
    }
}
