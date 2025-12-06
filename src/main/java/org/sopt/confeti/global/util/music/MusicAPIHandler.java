package org.sopt.confeti.global.util.music;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.sopt.confeti.global.resolver.music_api.artist.vo.ConfetiArtist;
import org.sopt.confeti.global.resolver.music_api.song.vo.ConfetiSong;
import org.sopt.confeti.global.util.music.dto.music.SongPage;

public interface MusicAPIHandler {

    List<ConfetiArtist> getArtistsByArtistIds(final Set<String> artistIds);

    List<ConfetiArtist> getRelatedArtists(final String artistId, final int limit);

    Optional<ConfetiArtist> findArtistByKeyword(final String keyword);

    List<ConfetiArtist> findArtistsByKeyword(final String keyword, final int limit);

    Optional<ConfetiArtist> findArtistByArtistId(final String artistId);

    List<ConfetiSong> getSongsBySongIds(final Set<String> songIds);

    List<ConfetiSong> getTopSongs(final int fetchSize);

    List<ConfetiSong> getFilteredTopSongsByArtist(String artistId, int limit,
        Set<String> excludedSongIds);

    SongPage getSongsByKeyword(String term, int offset, int limit);

    SongPage getArtistSongsByArtistId(String artistId, int offset, int limit);

    List<ConfetiSong> getArtistTopSongs(String id, int recommendSongFetchSize);
}
