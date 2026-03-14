package org.sopt.confeti.domain.music.relatedartist;

import java.util.List;
import org.sopt.confeti.global.resolver.music_api.artist.vo.ConfetiArtist;

public record CreateRelatedArtistsEvent(
    ConfetiArtist artist,
    List<ConfetiArtist> relatedArtists
) {

}
