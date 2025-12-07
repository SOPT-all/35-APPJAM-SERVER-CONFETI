package org.sopt.confeti.api.setlist.dto.response;

import java.time.LocalDate;
import java.util.List;
import org.sopt.confeti.domain.setlist.Setlist;
import org.sopt.confeti.domain.setlist.SetlistType;
import org.sopt.confeti.global.common.constant.FolderPath;
import org.sopt.confeti.global.util.S3FileHandler;

@Deprecated
public record GetSetlistDetailResponse_deprecated(
    Long setlistId,
    String type,
    Long typeId,
    String posterUrl,
    String title,
    String subTitle,
    LocalDate startAt,
    LocalDate endAt,
    List<SetlistSongResponse_deprecated> musics
) {

    public static GetSetlistDetailResponse_deprecated of(
        Setlist setlist,
        String title,
        String subtitle,
        String posterPath,
        LocalDate startAt,
        LocalDate endAt,
        List<SetlistSongResponse_deprecated> musics,
        SetlistType type,
        S3FileHandler s3FileHandler
    ) {
        String posterUrl = s3FileHandler.getFileUrl(
            FolderPath.combine(
                type == SetlistType.CONCERT ? FolderPath.CONCERT : FolderPath.FESTIVAL,
                FolderPath.POSTER
            ), posterPath).toString();

        return new GetSetlistDetailResponse_deprecated(
            setlist.getId(),
            type.name(),
            setlist.getTypeId(),
            posterUrl,
            title,
            subtitle,
            startAt,
            endAt,
            musics
        );
    }
}
