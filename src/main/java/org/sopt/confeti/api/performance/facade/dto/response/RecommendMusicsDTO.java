package org.sopt.confeti.api.performance.facade.dto.response;

import org.sopt.confeti.domain.view.performance.Performance;
import org.sopt.confeti.global.common.constant.PerformanceType;
import org.sopt.confeti.global.resolver.music_api.music.vo.ConfetiMusic;

import java.util.List;

public record RecommendMusicsDTO(
        Long id,
        Long typeId,
        PerformanceType type,
        String title,
        List<RecommendMusicDTO> musicList
) {
    public static RecommendMusicsDTO of(Performance performance, List<ConfetiMusic> musicList) {
        return new RecommendMusicsDTO(
                performance.getId(),
                performance.getTypeId(),
                performance.getType(),
                performance.getTitle(),
                musicList.stream()
                        .map(RecommendMusicDTO::from)
                        .toList()
        );
    }
}
