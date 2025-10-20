package org.sopt.confeti.api.performance.dto.response;

import java.util.List;
import org.sopt.confeti.api.performance.facade.dto.response.PerformanceRecommendDTO;
import org.sopt.confeti.global.common.constant.FolderPath;
import org.sopt.confeti.global.util.S3FileHandler;

public record PerformanceRecommendResponse(
        long typeId,
        String type,
        String title,
        String posterUrl,
        List<SongRecommendResponse> songs
) {
    public static PerformanceRecommendResponse of(PerformanceRecommendDTO performance, S3FileHandler s3FileHandler) {
        FolderPath topFolder = FolderPath.getFolderPathByPerformanceType(performance.type());

        return new PerformanceRecommendResponse(
                performance.typeId(),
                performance.type().getName(),
                performance.title(),
                s3FileHandler.getFileUrl(FolderPath.combine(topFolder, FolderPath.POSTER), performance.posterPath()).toString(),
                performance.songs().stream()
                        .map(SongRecommendResponse::from)
                        .toList()
        );
    }
}
