package org.sopt.confeti.api.user.dto.response;

import java.util.List;
import org.sopt.confeti.api.user.facade.dto.response.TimetableToAddDTO;
import org.sopt.confeti.global.common.CursorPage;
import org.sopt.confeti.global.util.S3FileHandler;

public record TimetablesToAddResponse(
        long nextCursor,
        List<TimetablesToAddFestivalResponse> festivals
) {
    private static final long DEFAULT_NEXT_CURSOR = -1L;

    public static TimetablesToAddResponse of(final CursorPage<TimetableToAddDTO> cursorPage, final S3FileHandler s3FileHandler) {
        Long nextCursor = DEFAULT_NEXT_CURSOR;

        if (!cursorPage.isLast()) {
            nextCursor = cursorPage.getNextCursor().festivalId();
        }

        return new TimetablesToAddResponse(
                nextCursor,
                cursorPage.getItems().stream()
                        .map(timetableToAddDTO -> TimetablesToAddFestivalResponse.of(timetableToAddDTO, s3FileHandler))
                        .toList()
        );
    }
}
