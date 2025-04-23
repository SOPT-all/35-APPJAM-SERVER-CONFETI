package org.sopt.confeti.domain.setlist.application.dto.response;

import java.time.LocalDate;
import org.sopt.confeti.domain.setlist.Setlist;

public record SetlistSummaryDto(
        Long setlistId,
        String type,
        Long typeId,
        String title,
        String posterUrl,
        LocalDate endAt
) {
    public static SetlistSummaryDto of(Setlist setlist, String title, String posterUrl, LocalDate endAt) {
        return new SetlistSummaryDto(
                setlist.getId(),
                setlist.getType().name(),
                setlist.getTypeId(),
                title,
                posterUrl,
                endAt
        );
    }
}
