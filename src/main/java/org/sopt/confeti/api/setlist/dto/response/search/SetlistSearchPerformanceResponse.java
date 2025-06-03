package org.sopt.confeti.api.setlist.dto.response.search;

import org.sopt.confeti.api.setlist.facade.dto.response.search.SearchPerformanceDTO;
import org.sopt.confeti.global.common.constant.FolderPath;
import org.sopt.confeti.global.util.S3FileHandler;

public record SetlistSearchPerformanceResponse(
        long performanceId,
        String type,
        long typeId,
        String title,
        String posterUrl
) {
    public static SetlistSearchPerformanceResponse of(SearchPerformanceDTO performanceDTO,
                                                      S3FileHandler s3FileHandler) {
        return new SetlistSearchPerformanceResponse(
                performanceDTO.id(),
                performanceDTO.type().getType().toUpperCase(),
                performanceDTO.typeId(),
                performanceDTO.title(),
                s3FileHandler.getFileUrl(
                        FolderPath.combine(FolderPath.getFolderPathByPerformanceType(performanceDTO.type()),
                                FolderPath.POSTER),
                        performanceDTO.posterPath()).toString()
        );
    }
}
