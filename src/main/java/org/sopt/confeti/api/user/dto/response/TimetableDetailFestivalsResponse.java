package org.sopt.confeti.api.user.dto.response;

import java.util.List;
import org.sopt.confeti.api.user.facade.dto.response.TimetableDetailFestivalsDTO;
import org.sopt.confeti.global.util.S3FileHandler;

public record TimetableDetailFestivalsResponse(
        List<TimetableDetailFestivalResponse> festivals
) {
    public static TimetableDetailFestivalsResponse of(
            TimetableDetailFestivalsDTO timetableDetailFestivalsDTO, S3FileHandler s3FileHandler) {
        return new TimetableDetailFestivalsResponse(
                timetableDetailFestivalsDTO.festivals().stream()
                        .map(timetableDTO -> TimetableDetailFestivalResponse.of(timetableDTO,
                                s3FileHandler))
                        .toList()
        );
    }
}
