package org.sopt.confeti.api.user.facade.dto.request;

import java.util.List;
import org.sopt.confeti.api.user.dto.request.AddTimetableFestivalRequest;

public record AddTimetableFestivalDTO(
        List<AddTimetableFestivalArtiestDTO> festivals
) {
    public static AddTimetableFestivalDTO from(final AddTimetableFestivalRequest addTimetableFestivalRequest) {
        return new AddTimetableFestivalDTO(
                addTimetableFestivalRequest.festivals().stream()
                        .map(AddTimetableFestivalArtiestDTO::from)
                        .toList()
        );
    }
}
