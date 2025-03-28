package org.sopt.confeti.global.util.music;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.sopt.confeti.global.resolver.artist.vo.ConfetiArtist;
import reactor.core.publisher.Mono;

public interface MusicAPIHandler {

    Mono<List<ConfetiArtist>> getArtistsByArtistIds(final Set<String> artistIds);
    Mono<Optional<ConfetiArtist>> findArtistByKeyword(final String keyword);
    Mono<Optional<ConfetiArtist>> findArtistByArtistId(final String artistId);
}
