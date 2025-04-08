package org.sopt.confeti.api.dummy.facade.dto.festival.response;

import java.time.LocalDate;
import org.sopt.confeti.domain.festival.Festival;

public record DummyFestivalPreviewDTO(
        long festivalId,
        String title,
        LocalDate startAt,
        LocalDate endAt,
        String posterPath,
        String area
) {
    public static DummyFestivalPreviewDTO from(Festival festival) {
        return new DummyFestivalPreviewDTO(
                festival.getId(),
                festival.getTitle(),
                festival.getStartAt(),
                festival.getEndAt(),
                festival.getPosterPath(),
                festival.getArea()
        );
    }
}
