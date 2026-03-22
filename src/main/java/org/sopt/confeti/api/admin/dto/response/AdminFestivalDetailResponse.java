package org.sopt.confeti.api.admin.dto.response;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import org.sopt.confeti.api.admin.facade.dto.response.AdminFestivalDetailInfo;
import org.sopt.confeti.api.admin.facade.dto.response.AdminFestivalDetailInfo.DateInfo;
import org.sopt.confeti.domain.festival.infra.TimetableSupportStatus;
import org.sopt.confeti.global.common.constant.FolderPath;
import org.sopt.confeti.global.util.S3FileHandler;

public record AdminFestivalDetailResponse(
    long festivalId,
    String title,
    LocalDate startAt,
    LocalDate endAt,
    String area,
    String posterUrl,
    String logoUrl,
    LocalDateTime reserveAt,
    String ageRating,
    String time,
    String price,
    String address,
    TimetableSupportStatus timetableSupportStatus,
    LocalDateTime createdAt,
    LocalDateTime updatedAt,
    List<DateResponse> dates
) {

    public record DateResponse(
        long festivalDateId,
        LocalDate festivalAt,
        LocalTime openAt,
        List<ArtistResponse> artists,
        List<StageResponse> stages
    ) {

        public static DateResponse from(AdminFestivalDetailInfo.DateInfo info) {
            return new DateResponse(
                info.festivalDateId(),
                info.festivalAt(),
                info.openAt(),
                info.artists().stream()
                    .map(ArtistResponse::from)
                    .toList(),
                info.stages().stream()
                    .map(StageResponse::from)
                    .toList()
            );
        }
    }

    public record StageResponse(
        long festivalStageId,
        String name,
        int order,
        List<TimeResponse> times
    ) {

        public static StageResponse from(AdminFestivalDetailInfo.StageInfo info) {
            return new StageResponse(
                info.festivalStageId(),
                info.name(),
                info.order(),
                info.times().stream()
                    .map(TimeResponse::from)
                    .toList()
            );
        }
    }

    public record TimeResponse(
        long festivalTimeId,
        LocalTime startAt,
        LocalTime endAt,
        List<ArtistResponse> artists
    ) {

        public static TimeResponse from(AdminFestivalDetailInfo.TimeInfo info) {
            return new TimeResponse(
                info.festivalTimeId(),
                info.startAt(),
                info.endAt(),
                info.artists().stream()
                    .map(ArtistResponse::from)
                    .toList()
            );
        }
    }

    public record ArtistResponse(
        String artistId,
        String name,
        String artworkUrl
    ) {

        public static ArtistResponse from(AdminFestivalDetailInfo.ArtistInfo info) {
            return new ArtistResponse(
                info.artistId(),
                info.name(),
                info.artworkUrl()
            );
        }
    }

    public static AdminFestivalDetailResponse of(AdminFestivalDetailInfo info, S3FileHandler s3FileHandler) {
        return new AdminFestivalDetailResponse(
            info.festivalId(),
            info.title(),
            info.startAt(),
            info.endAt(),
            info.area(),
            s3FileHandler.getFileUrl(FolderPath.combine(FolderPath.FESTIVAL, FolderPath.POSTER), info.posterPath()).toString(),
            s3FileHandler.getFileUrl(FolderPath.combine(FolderPath.FESTIVAL, FolderPath.LOGO), info.logoPath()).toString(),
            info.reserveAt(),
            info.ageRating(),
            info.time(),
            info.price(),
            info.address(),
            info.timetableSupportStatus(),
            info.createdAt(),
            info.updatedAt(),
            info.dates().stream()
                .map(DateResponse::from)
                .toList()
        );
    }
}
