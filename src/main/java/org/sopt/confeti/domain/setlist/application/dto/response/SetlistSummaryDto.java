package org.sopt.confeti.domain.setlist.application.dto.response;

import java.time.LocalDate;
import org.sopt.confeti.domain.setlist.Setlist;
import org.sopt.confeti.domain.setlist.SetlistType;
import org.sopt.confeti.global.common.constant.FolderPath;
import org.sopt.confeti.global.util.S3FileHandler;

public record SetlistSummaryDto(
        Long setlistId,
        String type,
        Long typeId,
        String title,
        String posterUrl,
        LocalDate endAt
) {
    public static SetlistSummaryDto of(
            Setlist setlist,
            String title,
            String posterPath,
            LocalDate endAt,
            S3FileHandler s3FileHandler
            ) {
        String posterUrl =  s3FileHandler.getFileUrl(
                FolderPath.combine(
                        setlist.getType() == SetlistType.CONCERT ? FolderPath.CONCERT : FolderPath.FESTIVAL, FolderPath.POSTER
                ), posterPath).toString();
        return new SetlistSummaryDto(
                setlist.getId(),
                setlist.getType().name(),
                setlist.getTypeId(),
                title,
                posterUrl,
                endAt
        );
    }
}
