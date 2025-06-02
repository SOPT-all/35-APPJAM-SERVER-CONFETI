package org.sopt.confeti.api.search.dto.response;

import java.time.LocalDate;
import org.sopt.confeti.api.search.facade.dto.response.SearchResultPerformanceDTO;
import org.sopt.confeti.global.common.constant.FolderPath;
import org.sopt.confeti.global.common.constant.PerformanceType;
import org.sopt.confeti.global.util.S3FileHandler;

public record SearchResultPerformanceResponse(
        long id,
        PerformanceType type,
        long typeId,
        String title,
        String posterUrl,
        LocalDate startAt,
        LocalDate endAt,
        String area,
        boolean isFavorite
) {
    public static SearchResultPerformanceResponse of(SearchResultPerformanceDTO performance,
                                                     S3FileHandler s3FileHandler) {
        return new SearchResultPerformanceResponse(
                performance.id(),
                performance.type(),
                performance.typeId(),
                performance.title(),
                s3FileHandler.getFileUrl(
                        FolderPath.combine(FolderPath.getFolderPathByPerformanceType(performance.type()),
                                FolderPath.POSTER), performance.posterPath()).toString(),
                performance.startAt(),
                performance.endAt(),
                performance.area(),
                performance.isFavorite()
        );
    }
}
