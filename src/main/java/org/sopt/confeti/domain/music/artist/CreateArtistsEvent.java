package org.sopt.confeti.domain.music.artist;

import java.util.List;
import org.sopt.confeti.global.resolver.music_api.artist.vo.ConfetiArtist;

public record CreateArtistsEvent(
        List<ConfetiArtist> artists
) {
}
