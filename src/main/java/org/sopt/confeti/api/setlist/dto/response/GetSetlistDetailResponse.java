package org.sopt.confeti.api.setlist.dto.response;

import java.time.LocalDate;
import java.util.List;
import org.sopt.confeti.api.setlist.facade.dto.response.SetlistDetailDTO;

public record GetSetlistDetailResponse(
    long setlistId,
    String type,
    long typeId,
    String posterUrl,
    String title,
    LocalDate startAt,
    LocalDate endAt,
    List<SetlistSongResponse> songs
) {

    public static GetSetlistDetailResponse from(SetlistDetailDTO dto) {
        return new GetSetlistDetailResponse(
            dto.setlistId(),
            dto.type(),
            dto.typeId(),
            dto.posterUrl(),
            dto.title(),
            dto.startAt(),
            dto.endAt(),
            dto.songs().stream()
                .map(SetlistSongResponse::from)
                .toList()
        );
    }
}
