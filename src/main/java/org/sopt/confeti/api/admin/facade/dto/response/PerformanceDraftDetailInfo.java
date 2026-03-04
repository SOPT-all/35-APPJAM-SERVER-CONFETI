package org.sopt.confeti.api.admin.facade.dto.response;

import java.util.List;
import org.sopt.confeti.domain.performancedraft.application.dto.response.PerformanceDraftDto;
import org.sopt.confeti.global.resolver.music_api.artist.vo.ConfetiArtist;

public record PerformanceDraftDetailInfo(
        PerformanceDraftDto draft,
        List<ConfetiArtist> artists
) {
}
