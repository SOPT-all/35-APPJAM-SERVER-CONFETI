package org.sopt.confeti.api.user.dto.response;

import org.sopt.confeti.api.user.facade.dto.response.UpcomingPerformanceDTO;
import org.sopt.confeti.global.common.constant.FolderPath;
import org.sopt.confeti.global.common.constant.PerformanceType;
import org.sopt.confeti.global.util.DateConvertor;
import org.sopt.confeti.global.util.S3FileHandler;

public record UpcomingPerformanceResponse(
        long typeId,
        PerformanceType type,
        String title,
        String posterUrl,
        String startAt,
        String endAt,
        String area
) {
    public static UpcomingPerformanceResponse of(final UpcomingPerformanceDTO upcomingPerformanceDTO,
                                                 final S3FileHandler s3FileHandler) {
        FolderPath topFolder = FolderPath.getFolderPathByPerformanceType(upcomingPerformanceDTO.type());
        return new UpcomingPerformanceResponse(
                upcomingPerformanceDTO.typeId(),
                upcomingPerformanceDTO.type(),
                upcomingPerformanceDTO.title(),
                s3FileHandler.getFileUrl(FolderPath.combine(topFolder, FolderPath.POSTER),
                                upcomingPerformanceDTO.posterPath())
                        .toString(),
                DateConvertor.convertToDefaultFormat(upcomingPerformanceDTO.startAt()),
                DateConvertor.convertToDefaultFormat(upcomingPerformanceDTO.endAt()),
                upcomingPerformanceDTO.area()
        );
    }
}
