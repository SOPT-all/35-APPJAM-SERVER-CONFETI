package org.sopt.confeti.global.util.music;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.sopt.confeti.global.resolver.music_api.album.vo.ConfetiAlbum;
import org.sopt.confeti.global.resolver.music_api.artist.vo.ConfetiArtist;
import org.sopt.confeti.global.resolver.music_api.music.vo.ConfetiMusic;

public interface MusicAPIHandler {

    List<ConfetiArtist> getArtistsByArtistIds(final Set<String> artistIds);

    List<ConfetiArtist> getRelatedArtists(final String artistId, final int limit);

    List<ConfetiArtist> findArtistsByKeyword(final String keyword, final int limit);

    Optional<ConfetiArtist> findArtistByArtistId(final String artistId);

    List<ConfetiAlbum> getAlbumsByAlbumIds(final Set<String> albumIds);

    List<ConfetiMusic> getMusicsByMusicIds(final Set<String> musicIds);

    List<ConfetiMusic> getTopMusics(final int fetchSize);

    List<ConfetiMusic> getMusicsByArtistIds(final Set<String> artistIds);
}
