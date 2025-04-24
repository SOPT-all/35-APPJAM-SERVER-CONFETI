package org.sopt.confeti.api.setlist.dto.response;

import org.sopt.confeti.api.setlist.facade.dto.response.SearchPerformanceDTO;
import org.sopt.confeti.global.common.constant.FolderPath;
import org.sopt.confeti.global.util.S3FileHandler;

public record SetlistSearchPerformanceResponse(
        long performanceId,
        String title,
        String posterUrl
) {
    public static SetlistSearchPerformanceResponse of(SearchPerformanceDTO performanceDTO,
                                                      S3FileHandler s3FileHandler) {
        return new SetlistSearchPerformanceResponse(
                performanceDTO.id(),
                performanceDTO.title(),
                s3FileHandler.getFileUrl(
                        FolderPath.combine(FolderPath.getFolderPathByPerformanceType(performanceDTO.type()),
                                FolderPath.POSTER),
                        performanceDTO.posterPath()).toString()
        );
    }
}
