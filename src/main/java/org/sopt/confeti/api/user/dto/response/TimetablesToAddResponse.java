package org.sopt.confeti.api.user.dto.response;

import java.util.List;

public record TimetablesToAddResponse(
        long nextCursor,
        List<TimetablesToAddFestivalResponse> festivals
) {
}
