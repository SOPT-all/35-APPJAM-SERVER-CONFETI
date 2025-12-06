package org.sopt.confeti.api.performance.facade.dto.response;

import java.util.List;
import org.sopt.confeti.global.resolver.music_api.song.vo.ConfetiSong;

public record RecommendSongsDTO(
    List<RecommendSongDTO> songList
) {

    public static RecommendSongsDTO from(List<ConfetiSong> songList) {
        return new RecommendSongsDTO(
            songList.stream()
                .map(RecommendSongDTO::from)
                .toList()
        );
    }
}
