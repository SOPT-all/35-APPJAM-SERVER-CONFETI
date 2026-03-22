package org.sopt.confeti.api.admin.dto.response;

import java.time.LocalDate;
import java.util.List;
import org.sopt.confeti.api.admin.facade.dto.response.AdminFestivalPreviewInfo;
import org.sopt.confeti.api.admin.facade.dto.response.AdminFestivalListInfo;
import org.sopt.confeti.global.common.constant.FolderPath;
import org.sopt.confeti.global.util.S3FileHandler;

public record AdminFestivalListResponse(
    FestivalGroupResponse upcomingFestivals,
    FestivalGroupResponse finishedFestivals
) {

    public record FestivalGroupResponse(
        List<FestivalResponse> festivals,
        int count
    ) {

        public static FestivalGroupResponse of(
            List<AdminFestivalPreviewInfo> festivals,
            S3FileHandler s3FileHandler
        ) {
            List<FestivalResponse> responses = festivals.stream()
                .map(f -> FestivalResponse.of(f, s3FileHandler))
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

        public static FestivalResponse of(
            AdminFestivalPreviewInfo info,
            S3FileHandler s3FileHandler
        ) {
            return new FestivalResponse(
                info.festivalId(),
                s3FileHandler.getFileUrl(
                    FolderPath.combine(FolderPath.FESTIVAL, FolderPath.POSTER),
                    info.posterPath()
                ).toString(),
                info.title(),
                info.startAt(),
                info.endAt(),
                info.area()
            );
        }
    }

    public static AdminFestivalListResponse of(
        AdminFestivalListInfo info,
        S3FileHandler s3FileHandler
    ) {
        return new AdminFestivalListResponse(
            FestivalGroupResponse.of(info.upcomingFestivals(), s3FileHandler),
            FestivalGroupResponse.of(info.finishedFestivals(), s3FileHandler)
        );
    }
}
