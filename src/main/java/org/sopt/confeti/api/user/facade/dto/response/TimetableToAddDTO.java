package org.sopt.confeti.api.user.facade.dto.response;


import org.sopt.confeti.global.mapper.dto.festival.Festival;

public record TimetableToAddDTO(
        long performanceId,
        String posterUrl,
        String title
) {
    public static TimetableToAddDTO from(Festival festival) {
        return new TimetableToAddDTO(
                festival.performanceId(),
                festival.posterUrl(),
                festival.title()
        );
    }
}
