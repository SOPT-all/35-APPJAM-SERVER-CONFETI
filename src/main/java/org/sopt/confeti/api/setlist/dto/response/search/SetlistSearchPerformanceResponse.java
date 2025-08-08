package org.sopt.confeti.api.setlist.dto.response.search;

import org.sopt.confeti.api.setlist.facade.dto.response.search.SearchedPerformanceDTO;
import org.sopt.confeti.global.common.constant.FolderPath;
import org.sopt.confeti.global.util.S3FileHandler;

public record SetlistSearchPerformanceResponse(
        long performanceId,
        String type,
        String title,
        String posterUrl
) {
    public static SetlistSearchPerformanceResponse from(SearchedPerformanceDTO performanceDTO) {
        return new SetlistSearchPerformanceResponse(
                performanceDTO.id(),
                performanceDTO.type().getName().toUpperCase(),
                performanceDTO.title(),
                performanceDTO.posterUrl()
        );
    }
}
