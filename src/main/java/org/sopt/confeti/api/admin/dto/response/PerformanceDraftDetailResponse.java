package org.sopt.confeti.api.admin.dto.response;

import com.fasterxml.jackson.annotation.JsonRawValue;
import java.util.List;
import java.util.Optional;
import org.sopt.confeti.api.admin.facade.dto.response.PerformanceDraftDetailInfo;
import org.sopt.confeti.domain.performancedraft.DraftStatus;
import org.sopt.confeti.domain.performancedraft.PerformanceDraftType;
import org.sopt.confeti.global.common.constant.FolderPath;
import org.sopt.confeti.global.resolver.music_api.artist.vo.ConfetiArtist;
import org.sopt.confeti.global.util.S3FileHandler;

public record PerformanceDraftDetailResponse(
        Long id,
        PerformanceDraftType performanceType,
        DraftStatus status,
        @JsonRawValue
        String performanceData,
        String posterUrl,
        String logoUrl,
        List<ArtistResponse> artists
) {
    public record ArtistResponse(
            String artistId,
            String name,
            String profileUrl
    ) {
        public static ArtistResponse from(ConfetiArtist artist) {
            return new ArtistResponse(
                    artist.getId(),
                    artist.getName(),
                    artist.getProfileUrl()
            );
        }
    }

    public static PerformanceDraftDetailResponse from(PerformanceDraftDetailInfo info, S3FileHandler s3FileHandler) {
        return new PerformanceDraftDetailResponse(
                info.draft().id(),
                info.draft().performanceDraftType(),
                info.draft().status(),
                info.draft().performanceData(),
                Optional.ofNullable(info.draft().posterUrl())
                        .map(path -> s3FileHandler.getFileUrl(FolderPath.combine(FolderPath.PERFORMANCE_DRAFT, FolderPath.POSTER), path).toString())
                        .orElse(null),
                Optional.ofNullable(info.draft().logoUrl())
                        .map(path -> s3FileHandler.getFileUrl(FolderPath.combine(FolderPath.PERFORMANCE_DRAFT, FolderPath.LOGO), path).toString())
                        .orElse(null),
                info.artists().stream()
                        .map(ArtistResponse::from)
                        .toList()
        );
    }
}
