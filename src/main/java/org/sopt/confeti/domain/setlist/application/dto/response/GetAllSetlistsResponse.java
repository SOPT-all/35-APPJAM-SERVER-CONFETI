package org.sopt.confeti.domain.setlist.application.dto.response;

import java.util.List;

public record GetAllSetlistsResponse(
        int totalCount,
        List<SetlistSummaryDto> setlists
) {
}
