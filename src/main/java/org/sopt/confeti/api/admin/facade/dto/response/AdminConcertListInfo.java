package org.sopt.confeti.api.admin.facade.dto.response;

import java.time.LocalDate;
import java.util.List;
import org.sopt.confeti.domain.concert.application.dto.ConcertPreviewInfo;

public record AdminConcertListInfo(
    List<ConcertInfo> upcomingConcerts,
    List<ConcertInfo> finishedConcerts
) {

    public record ConcertInfo(
        long concertId,
        String posterPath,
        String title,
        String subtitle,
        LocalDate startAt,
        LocalDate endAt,
        String area
    ) {

        public static ConcertInfo from(ConcertPreviewInfo info) {
            return new ConcertInfo(
                info.concertId(),
                info.posterPath(),
                info.title(),
                info.subtitle(),
                info.startAt(),
                info.endAt(),
                info.area()
            );
        }
    }
}
