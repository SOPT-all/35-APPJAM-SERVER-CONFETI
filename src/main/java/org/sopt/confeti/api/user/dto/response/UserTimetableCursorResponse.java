package org.sopt.confeti.api.user.dto.response;

import java.time.LocalDate;
import java.util.List;
import org.sopt.confeti.domain.festival.Festival;
import org.sopt.confeti.domain.timetable_festival.TimetableFestival;
import org.sopt.confeti.domain.timetable_festival.TimetableFestivalCursor;
import org.sopt.confeti.global.common.CursorPage;
import org.sopt.confeti.global.common.constant.FolderPath;
import org.sopt.confeti.global.util.S3FileHandler;

public record UserTimetableCursorResponse(
        String nextCursor,
        List<UserTimetableResponse> timetables
) {
    private static final String LAST_CURSOR = "empty";

    public record UserTimetableResponse(
            long timetableFestivalId,
            String posterUrl,
            String title,
            LocalDate startAt
    ) {
        public static UserTimetableResponse of(TimetableFestival timetableFestival, S3FileHandler s3FileHandler) {
            Festival festival = timetableFestival.getFestival();

            String posterUrl = s3FileHandler.getFileUrl(
                    FolderPath.combine(FolderPath.FESTIVAL, FolderPath.POSTER),
                    festival.getPosterPath()
            ).toString();

            return new UserTimetableResponse(
                    timetableFestival.getId(),
                    posterUrl,
                    festival.getTitle(),
                    festival.getStartAt()
            );
        }
    }

    public static UserTimetableCursorResponse of(CursorPage<TimetableFestival> cursorPage, S3FileHandler s3FileHandler) {
        TimetableFestival nextTimetableFestival = cursorPage.getNextCursor();
        String nextCursor = cursorPage.isLast() ? LAST_CURSOR : TimetableFestivalCursor.encode(nextTimetableFestival.getFestival().getStartAt(), nextTimetableFestival.getId());

        return new UserTimetableCursorResponse(
                nextCursor,
                cursorPage.getItems().stream()
                        .map(item -> UserTimetableResponse.of(item, s3FileHandler))
                        .toList()
        );
    }
}
