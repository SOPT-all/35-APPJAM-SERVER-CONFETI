package org.sopt.confeti.api.performance.facade.dto.response;

import org.sopt.confeti.global.resolver.music_api.music.vo.ConfetiMusic;
import java.util.List;

public record RecommendMusicsDTO(
        List<RecommendMusicDTO> musicList
) {
    public static RecommendMusicsDTO from(List<ConfetiMusic> musicList) {
        return new RecommendMusicsDTO(
                musicList.stream()
                        .map(RecommendMusicDTO::from)
                        .toList()
        );
    }
}