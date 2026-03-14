package org.sopt.confeti.domain.music.song;

import java.util.List;
import org.sopt.confeti.global.resolver.music_api.song.vo.ConfetiSong;

public record CreateSongsEvent(
        List<ConfetiSong> songs
) {
}
