package org.sopt.confeti.api.user.facade.dto.response;

import org.sopt.confeti.domain.timetablefestival.TimetableFestival;

import java.util.List;

public record UserTimetableFestivalDTO(
        long festivalId,
        String title,
        String logoPath,
        List<UserTimetableDatesDTO> festivalDates
) {
    public static UserTimetableFestivalDTO from(TimetableFestival timetableFestival) {
        return new UserTimetableFestivalDTO(
                timetableFestival.getFestival().getId(),
                timetableFestival.getFestival().getFestivalTitle(),
                timetableFestival.getFestival().getFestivalLogoPath(),
                timetableFestival.getFestival().getDates().stream()
                        .map(UserTimetableDatesDTO::from)
                        .toList()
        );
    }

}
