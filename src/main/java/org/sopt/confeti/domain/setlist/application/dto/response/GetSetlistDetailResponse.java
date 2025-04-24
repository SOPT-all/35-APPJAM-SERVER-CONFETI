package org.sopt.confeti.domain.setlist.application.dto.response;

import java.time.LocalDate;
import java.util.List;

public record GetSetlistDetailResponse(
        Long setlistId,
        String type,
        Long typeId,
        String posterUrl,
        String posterBgUrl,
        String title,
        String subTitle,
        LocalDate startAt,
        LocalDate endAt,
        List<SetlistMusicResponseDto> musics
) {
}
