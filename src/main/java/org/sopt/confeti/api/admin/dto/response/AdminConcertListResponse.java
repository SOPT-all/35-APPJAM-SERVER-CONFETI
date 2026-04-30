package org.sopt.confeti.api.admin.dto.response;

import java.time.LocalDate;
import java.util.List;
import org.sopt.confeti.api.admin.facade.dto.response.AdminConcertListInfo;
import org.sopt.confeti.api.admin.facade.dto.response.AdminConcertListInfo.ConcertInfo;

public record AdminConcertListResponse(
    UpcomingConcerts upcomingConcerts,
    FinishedConcerts finishedConcerts
) {

    public static AdminConcertListResponse from(AdminConcertListInfo info) {
        return new AdminConcertListResponse(
            UpcomingConcerts.from(info.upcomingConcerts()),
            FinishedConcerts.from(info.finishedConcerts())
        );
    }

    public record UpcomingConcerts(
        List<ConcertResponse> concerts,
        int count
    ) {

        public static UpcomingConcerts from(List<ConcertInfo> infos) {
            List<ConcertResponse> concerts = infos.stream()
                .map(ConcertResponse::from)
                .toList();
            return new UpcomingConcerts(concerts, concerts.size());
        }
    }

    public record FinishedConcerts(
        List<ConcertResponse> concerts,
        int count
    ) {

        public static FinishedConcerts from(List<ConcertInfo> infos) {
            List<ConcertResponse> concerts = infos.stream()
                .map(ConcertResponse::from)
                .toList();
            return new FinishedConcerts(concerts, concerts.size());
        }
    }

    public record ConcertResponse(
        long concertId,
        String posterUrl,
        String title,
        LocalDate startAt,
        LocalDate endAt,
        String area
    ) {

        public static ConcertResponse from(ConcertInfo info) {
            return new ConcertResponse(
                info.concertId(),
                info.posterUrl(),
                info.title(),
                info.startAt(),
                info.endAt(),
                info.area()
            );
        }
    }
}
