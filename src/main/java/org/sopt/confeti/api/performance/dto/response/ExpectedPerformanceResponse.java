package org.sopt.confeti.api.performance.dto.response;

import org.sopt.confeti.api.performance.facade.dto.response.ExpectedPerformanceDTO;
import org.sopt.confeti.global.common.constant.FolderPath;
import org.sopt.confeti.global.util.S3FileHandler;

public record ExpectedPerformanceResponse(
        long performanceId,
        String type,
        long typeId,
        String title,
        String posterUrl
) {
    public static ExpectedPerformanceResponse of(ExpectedPerformanceDTO expectedPerformanceDTO,
                                                 S3FileHandler s3FileHandler) {
        return new ExpectedPerformanceResponse(
                expectedPerformanceDTO.id(),
                expectedPerformanceDTO.type().getName(),
                expectedPerformanceDTO.typeId(),
                expectedPerformanceDTO.title(),
                s3FileHandler.getFileUrl(
                        FolderPath.combine(FolderPath.getFolderPathByPerformanceType(expectedPerformanceDTO.type()),
                                FolderPath.POSTER),
                        expectedPerformanceDTO.posterPath()).toString()
        );
    }
}
