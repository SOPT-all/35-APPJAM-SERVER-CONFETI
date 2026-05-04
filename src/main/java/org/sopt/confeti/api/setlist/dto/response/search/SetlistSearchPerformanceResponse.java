package org.sopt.confeti.api.setlist.dto.response.search;

import org.sopt.confeti.api.setlist.facade.dto.response.search.SearchPerformanceDTO;

public record SetlistSearchPerformanceResponse(
    long performanceId,
    String type,
    long typeId,
    String title,
    String posterUrl
) {

    public static SetlistSearchPerformanceResponse from(SearchPerformanceDTO performanceDTO) {
        return new SetlistSearchPerformanceResponse(
            performanceDTO.id(),
            performanceDTO.type().getName().toUpperCase(),
            performanceDTO.typeId(),
            performanceDTO.title(),
            performanceDTO.posterUrl()
        );
    }
}
