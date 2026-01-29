package org.sopt.confeti.api.user.dto.response;

import java.util.List;
import org.sopt.confeti.api.user.facade.dto.response.TimetablesDTO;
import org.sopt.confeti.global.util.S3FileHandler;

public record TimetablesPreviewResponse(
        List<TimetableResponse> timetables
) {
    public static TimetablesPreviewResponse of(final TimetablesDTO timetableDTO, S3FileHandler s3FileHandler) {
        return new TimetablesPreviewResponse(
                timetableDTO.timetables().stream()
                        .map(timetable -> TimetableResponse.of(timetable, s3FileHandler))
                        .toList()
        );
    }
}