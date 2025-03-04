package org.sopt.confeti.api.performance.dto.response;

import org.sopt.confeti.api.performance.facade.dto.response.RecentPerformanceDTO;
import org.sopt.confeti.global.common.constant.FolderPath;
import org.sopt.confeti.global.util.DateConvertor;
import org.sopt.confeti.global.util.S3FileHandler;

public record RecentPerformanceResponse(
        long performanceId,
        long typeId,
        String type,
        String title,
        String subtitle,
        String performanceAt,
        String posterUrl
) {
    public static RecentPerformanceResponse of(final RecentPerformanceDTO recentPerformanceDTO, final S3FileHandler s3FileHandler) {
        FolderPath topFolder = FolderPath.getFolderPathByPerformanceType(recentPerformanceDTO.type());

        return new RecentPerformanceResponse(
                recentPerformanceDTO.performanceId(),
                recentPerformanceDTO.typeId(),
                recentPerformanceDTO.type().getType(),
                recentPerformanceDTO.title(),
                recentPerformanceDTO.subtitle(),
                DateConvertor.convertToLocalDate(recentPerformanceDTO.performanceAt()),
                s3FileHandler.getFileUrl(FolderPath.combine(topFolder, FolderPath.POSTER), recentPerformanceDTO.posterPath()).getPath()
        );
    }
}
