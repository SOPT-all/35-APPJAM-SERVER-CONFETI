package org.sopt.confeti.api.performance.dto.response;

import org.sopt.confeti.api.performance.facade.dto.response.ArtistPerformancesDetailDTO;
import org.sopt.confeti.global.common.constant.FolderPath;
import org.sopt.confeti.global.common.constant.PerformanceType;
import org.sopt.confeti.global.util.DateConvertor;
import org.sopt.confeti.global.util.S3FileHandler;

public record ArtistPerformancesDetailResponse(
        long performanceId,
        long typeId,
        PerformanceType type,
        String title,
        String startAt,
        String endAt,
        String posterUrl,
        String area,
        boolean isFavorite
) {
    public static ArtistPerformancesDetailResponse of(ArtistPerformancesDetailDTO performance,
                                                      S3FileHandler s3FileHandler) {
        FolderPath topFolder = FolderPath.getFolderPathByPerformanceType(performance.type());

        return new ArtistPerformancesDetailResponse(
                performance.performanceId(),
                performance.typeId(),
                performance.type(),
                performance.title(),
                DateConvertor.convertToDefaultFormat(performance.startAt()),
                DateConvertor.convertToDefaultFormat(performance.endAt()),
                s3FileHandler.getFileUrl(FolderPath.combine(topFolder, FolderPath.POSTER), performance.posterPath())
                        .toString(),
                performance.area(),
                performance.isFavorite()
        );
    }
}
