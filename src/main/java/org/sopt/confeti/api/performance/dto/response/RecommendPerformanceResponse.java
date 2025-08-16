package org.sopt.confeti.api.performance.dto.response;

import org.sopt.confeti.api.performance.facade.dto.response.RecommendPerformanceDTO;
import org.sopt.confeti.global.common.constant.FolderPath;
import org.sopt.confeti.global.util.S3FileHandler;

public record RecommendPerformanceResponse(
        long typeId,
        String type,
        String title,
        String posterUrl
) {
    public static RecommendPerformanceResponse of(final RecommendPerformanceDTO recommendPerformanceDTO,
                                                  final S3FileHandler s3FileHandler) {
        FolderPath topFolder = FolderPath.getFolderPathByPerformanceType(recommendPerformanceDTO.type());

        return new RecommendPerformanceResponse(
                recommendPerformanceDTO.typeId(),
                recommendPerformanceDTO.type().getName(),
                recommendPerformanceDTO.title(),
                s3FileHandler.getFileUrl(FolderPath.combine(topFolder, FolderPath.POSTER),
                        recommendPerformanceDTO.posterPath()).toString()
        );
    }
}

