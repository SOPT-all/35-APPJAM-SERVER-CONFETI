package org.sopt.confeti.global.util.music;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.sopt.confeti.global.resolver.artist.ConfetiArtist;

public interface MusicAPIHandler {

    List<ConfetiArtist> findArtistsByArtistIds(final Set<String> artistIds);
    Optional<ConfetiArtist> findArtistByKeyword(final String keyword);
    Optional<ConfetiArtist> findArtistByArtistId(final String artistId);
}
