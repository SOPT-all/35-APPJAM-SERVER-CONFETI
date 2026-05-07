package org.sopt.confeti.api.admin.dto.response;

import com.fasterxml.jackson.annotation.JsonRawValue;
import java.util.List;
import org.sopt.confeti.api.admin.facade.dto.response.PerformanceDraftDetailInfo;
import org.sopt.confeti.domain.performancedraft.DraftStatus;
import org.sopt.confeti.domain.performancedraft.PerformanceDraftType;
import org.sopt.confeti.global.resolver.music_api.artist.vo.ConfetiArtist;

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

    public static PerformanceDraftDetailResponse from(PerformanceDraftDetailInfo info) {
        return new PerformanceDraftDetailResponse(
                info.draft().id(),
                info.draft().performanceDraftType(),
                info.draft().status(),
                info.draft().performanceData(),
                info.draft().posterUrl(),
                info.draft().logoUrl(),
                info.artists().stream()
                        .map(ArtistResponse::from)
                        .toList()
        );
    }
}
