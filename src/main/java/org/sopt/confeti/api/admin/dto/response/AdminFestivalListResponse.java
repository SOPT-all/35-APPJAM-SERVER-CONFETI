package org.sopt.confeti.api.admin.dto.response;

import java.time.LocalDate;
import java.util.List;
import org.sopt.confeti.api.admin.facade.dto.response.AdminFestivalPreviewInfo;
import org.sopt.confeti.api.admin.facade.dto.response.AdminFestivalListInfo;

public record AdminFestivalListResponse(
    FestivalGroupResponse upcomingFestivals,
    FestivalGroupResponse finishedFestivals
) {

    public record FestivalGroupResponse(
        List<FestivalResponse> festivals,
        int count
    ) {

        public static FestivalGroupResponse from(List<AdminFestivalPreviewInfo> festivals) {
            List<FestivalResponse> responses = festivals.stream()
                .map(FestivalResponse::from)
                .toList();
            return new FestivalGroupResponse(responses, responses.size());
        }
    }

    public record FestivalResponse(
        long festivalId,
        String posterUrl,
        String title,
        LocalDate startAt,
        LocalDate endAt,
        String area
    ) {

        public static FestivalResponse from(AdminFestivalPreviewInfo info) {
            return new FestivalResponse(
                info.festivalId(),
                info.posterUrl(),
                info.title(),
                info.startAt(),
                info.endAt(),
                info.area()
            );
        }
    }

    public static AdminFestivalListResponse from(AdminFestivalListInfo info) {
        return new AdminFestivalListResponse(
            FestivalGroupResponse.from(info.upcomingFestivals()),
            FestivalGroupResponse.from(info.finishedFestivals())
        );
    }
}
