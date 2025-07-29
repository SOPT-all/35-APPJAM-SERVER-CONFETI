package org.sopt.confeti.api.performance.dto.response;

import org.sopt.confeti.api.performance.facade.dto.response.SearchACPerformanceDTO;
import org.sopt.confeti.global.common.constant.FolderPath;
import org.sopt.confeti.global.common.constant.PerformanceType_DEPRECATED;
import org.sopt.confeti.global.util.S3FileHandler;

public record SearchACPerformanceResponse(
        long id,
        String title,
        String posterUrl
) {
    public static SearchACPerformanceResponse of(SearchACPerformanceDTO performanceDTO, S3FileHandler s3FileHandler) {
        String folderPath = FolderPath.DEFAULT.getSingle();

        if (performanceDTO.type() == PerformanceType_DEPRECATED.FESTIVAL) {
            folderPath = FolderPath.combine(FolderPath.FESTIVAL, FolderPath.POSTER);
        }

        if (performanceDTO.type() == PerformanceType_DEPRECATED.CONCERT) {
            folderPath = FolderPath.combine(FolderPath.CONCERT, FolderPath.POSTER);
        }

        return new SearchACPerformanceResponse(
                performanceDTO.id(),
                performanceDTO.title(),
                s3FileHandler.getFileUrl(folderPath, performanceDTO.posterPath()).toString()
        );
    }
}
