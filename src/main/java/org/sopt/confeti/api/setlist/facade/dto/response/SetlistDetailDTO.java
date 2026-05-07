package org.sopt.confeti.api.setlist.facade.dto.response;

import java.time.LocalDate;
import java.util.List;
import org.sopt.confeti.domain.setlist.Setlist;
import org.sopt.confeti.domain.setlist.SetlistType;

public record SetlistDetailDTO(
    long setlistId,
    String type,
    long typeId,
    String posterUrl,
    String title,
    LocalDate startAt,
    LocalDate endAt,
    List<SetlistSongDTO> songs
) {

    public static SetlistDetailDTO of(
        Setlist setlist,
        String title,
        String posterUrl,
        LocalDate startAt,
        LocalDate endAt,
        List<SetlistSongDTO> songs,
        SetlistType type
    ) {
        return new SetlistDetailDTO(
            setlist.getId(),
            type.name(),
            setlist.getTypeId(),
            posterUrl,
            title,
            startAt,
            endAt,
            songs
        );
    }
}
