package org.sopt.confeti.api.user.dto.response.timetable;

import java.util.List;
import org.sopt.confeti.api.user.facade.dto.response.timetable.TimetableToAddDTO;
import org.sopt.confeti.global.common.CursorPage;

public record TimetablesToAddResponse(
    long nextCursor,
    List<TimetablesToAddFestivalResponse> festivals
) {

    private static final long DEFAULT_NEXT_CURSOR = -1L;

    public static TimetablesToAddResponse from(final CursorPage<TimetableToAddDTO> cursorPage) {
        Long nextCursor = DEFAULT_NEXT_CURSOR;

        if (!cursorPage.isLast()) {
            nextCursor = cursorPage.getNextCursor().festivalId();
        }

        return new TimetablesToAddResponse(
            nextCursor,
            cursorPage.getItems().stream()
                .map(TimetablesToAddFestivalResponse::from)
                .toList()
        );
    }
}
