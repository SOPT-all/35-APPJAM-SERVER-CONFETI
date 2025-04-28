package org.sopt.confeti.domain.setlist.application.dto.response;

import java.time.LocalDate;
import java.util.List;
import org.sopt.confeti.domain.setlist.Setlist;
import org.sopt.confeti.domain.setlist.SetlistType;
import org.sopt.confeti.global.common.constant.FolderPath;
import org.sopt.confeti.global.util.S3FileHandler;

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
    public static GetSetlistDetailResponse of(
            Setlist setlist,
            String title,
            String subtitle,
            String posterPath,
            String posterBgPath,
            LocalDate startAt,
            LocalDate endAt,
            List<SetlistMusicResponseDto> musics,
            SetlistType type,
            S3FileHandler s3FileHandler
    ) {
        String posterUrl = s3FileHandler.getFileUrl(
                FolderPath.combine(
                        type == SetlistType.CONCERT ? FolderPath.CONCERT : FolderPath.FESTIVAL,
                        FolderPath.POSTER
                ), posterPath).toString();

        String posterBgUrl = s3FileHandler.getFileUrl(
                FolderPath.combine(
                        type == SetlistType.CONCERT ? FolderPath.CONCERT : FolderPath.FESTIVAL,
                        FolderPath.POSTER_BG
                ), posterBgPath).toString();

        return new GetSetlistDetailResponse(
                setlist.getId(),
                type.name(),
                setlist.getTypeId(),
                posterUrl,
                posterBgUrl,
                title,
                subtitle,
                startAt,
                endAt,
                musics
        );
    }
}
