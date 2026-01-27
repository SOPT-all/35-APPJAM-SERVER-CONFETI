package org.sopt.confeti.api.user.dto.response;

import java.time.LocalDate;
import java.util.List;
import org.sopt.confeti.domain.festival.Festival;
import org.sopt.confeti.domain.timetable.Timetable;
import org.sopt.confeti.domain.timetable.TimetableCursor;
import org.sopt.confeti.global.common.CursorPage;
import org.sopt.confeti.global.common.constant.FolderPath;
import org.sopt.confeti.global.util.S3FileHandler;

public record TimetableCursorResponse(
        String nextCursor,
        List<TimetableCursorItemResponse> timetables
) {
    private static final String LAST_CURSOR = "empty";

    public record TimetableCursorItemResponse(
            long timetableFestivalId,
            String posterUrl,
            String title,
            LocalDate startAt
    ) {
        public static TimetableCursorItemResponse of(Timetable timetable, S3FileHandler s3FileHandler) {
            Festival festival = timetable.getFestival();

            String posterUrl = s3FileHandler.getFileUrl(
                    FolderPath.combine(FolderPath.FESTIVAL, FolderPath.POSTER),
                    festival.getPosterPath()
            ).toString();

            return new TimetableCursorItemResponse(
                    timetable.getId(),
                    posterUrl,
                    festival.getTitle(),
                    festival.getStartAt()
            );
        }
    }

    public static TimetableCursorResponse of(CursorPage<Timetable> cursorPage, S3FileHandler s3FileHandler) {
        Timetable nextTimetable = cursorPage.getNextCursor();
        String nextCursor = cursorPage.isLast() ? LAST_CURSOR : TimetableCursor.encode(nextTimetable.getFestival().getStartAt(), nextTimetable.getId());

        return new TimetableCursorResponse(
                nextCursor,
                cursorPage.getItems().stream()
                        .map(item -> TimetableCursorItemResponse.of(item, s3FileHandler))
                        .toList()
        );
    }
}
