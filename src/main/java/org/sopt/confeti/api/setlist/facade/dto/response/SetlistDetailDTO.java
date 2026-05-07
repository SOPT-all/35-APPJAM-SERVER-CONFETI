package org.sopt.confeti.api.setlist.facade.dto.response;

import java.time.LocalDate;
import java.util.List;
import lombok.Builder;
import org.sopt.confeti.api.setlist.dto.response.SetlistSongResponse;
import org.sopt.confeti.domain.setlist.Setlist;
import org.sopt.confeti.domain.setlist.SetlistType;

@Builder
public record SetlistDetailDTO(
    long setlistId,
    String type,
    long typeId,
    String posterUrl,
    String title,
    LocalDate startAt,
    LocalDate endAt,
    List<SetlistSongResponse> songs
) {

    public static SetlistDetailDTO of(
        Setlist setlist,
        String title,
        String posterUrl,
        LocalDate startAt,
        LocalDate endAt,
        List<SetlistSongResponse> songs,
        SetlistType type
    ) {
        return SetlistDetailDTO.builder()
            .setlistId(setlist.getId())
            .type(type.name())
            .typeId(setlist.getTypeId())
            .posterUrl(posterUrl)
            .title(title)
            .startAt(startAt)
            .endAt(endAt)
            .songs(songs)
            .build();
    }
}
