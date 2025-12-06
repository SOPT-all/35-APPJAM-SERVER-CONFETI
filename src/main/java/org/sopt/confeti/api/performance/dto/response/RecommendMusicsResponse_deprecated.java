package org.sopt.confeti.api.performance.dto.response;

import java.util.List;
import org.sopt.confeti.api.performance.facade.dto.response.RecommendSongsDTO;

@Deprecated
public record RecommendMusicsResponse_deprecated(
    List<RecommendMusicResponse_deprecated> musics
) {

    public static RecommendMusicsResponse_deprecated from(RecommendSongsDTO musicListDTO) {
        return new RecommendMusicsResponse_deprecated(
            musicListDTO.songList().stream()
                .map(RecommendMusicResponse_deprecated::from)
                .toList()
        );
    }
}
