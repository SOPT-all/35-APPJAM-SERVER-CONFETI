package org.sopt.confeti.api.performance.facade.dto.response;

import org.sopt.confeti.global.resolver.music_api.music.vo.ConfetiMusic;
import java.util.List;

public record RecommendNewMusicsDTO(
        List<RecommendMusicDTO> musicList
) {
    public static RecommendNewMusicsDTO from(List<ConfetiMusic> musicList) {
        return new RecommendNewMusicsDTO(
                musicList.stream()
                        .map(RecommendMusicDTO::from)
                        .toList()
        );
    }
}