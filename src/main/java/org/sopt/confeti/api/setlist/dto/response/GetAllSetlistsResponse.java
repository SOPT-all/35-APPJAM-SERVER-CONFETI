package org.sopt.confeti.api.setlist.dto.response;

import java.util.List;

public record GetAllSetlistsResponse(
        int totalCount,
        List<SetlistSummaryResponse> setlists
) {
}
