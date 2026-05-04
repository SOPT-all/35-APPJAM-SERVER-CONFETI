package org.sopt.confeti.api.setlist.dto.response;

import java.time.LocalDate;
import org.sopt.confeti.domain.setlist.Setlist;
import org.sopt.confeti.domain.view.performance.Performance;

public record SetlistSummaryResponse(
    Long setlistId,
    String type,
    Long typeId,
    String title,
    String posterUrl,
    LocalDate startAt,
    LocalDate endAt
) {

    public static SetlistSummaryResponse of(
        Setlist setlist,
        Performance performance,
        String posterUrl
    ) {
        return new SetlistSummaryResponse(
            setlist.getId(),
            setlist.getType().name(),
            setlist.getTypeId(),
            performance.getTitle(),
            posterUrl,
            performance.getStartAt(),
            performance.getEndAt()
        );
    }
}
