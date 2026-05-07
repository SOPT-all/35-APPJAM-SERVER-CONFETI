package org.sopt.confeti.api.user.dto.response.timetable;

import java.time.LocalDate;
import java.util.List;
import org.sopt.confeti.api.user.facade.dto.response.timetable.TimetableCursorItemDTO;
import org.sopt.confeti.domain.timetable.TimetableCursor;
import org.sopt.confeti.global.common.CursorPage;

public record TimetableCursorResponse(
    String nextCursor,
    List<TimetableCursorItemResponse> timetables
) {

    private static final String LAST_CURSOR = "empty";

    public static TimetableCursorResponse from(CursorPage<TimetableCursorItemDTO> cursorPage) {
        TimetableCursorItemDTO nextItem = cursorPage.getNextCursor();
        String nextCursor = cursorPage.isLast() ? LAST_CURSOR
            : TimetableCursor.encode(nextItem.startAt(), nextItem.timetableId());

        return new TimetableCursorResponse(
            nextCursor,
            cursorPage.getItems().stream()
                .map(TimetableCursorItemResponse::from)
                .toList()
        );
    }

    public record TimetableCursorItemResponse(
        long timetableId,
        String posterUrl,
        String title,
        LocalDate startAt
    ) {

        public static TimetableCursorItemResponse from(TimetableCursorItemDTO dto) {
            return new TimetableCursorItemResponse(
                dto.timetableId(),
                dto.posterUrl(),
                dto.title(),
                dto.startAt()
            );
        }
    }
}
