package org.sopt.confeti.domain.music.topartist.application;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.sopt.confeti.domain.music.application.MusicAPIService;
import org.sopt.confeti.domain.music.application.dto.CacheResult;
import org.sopt.confeti.domain.music.application.dto.FetchResult;
import org.sopt.confeti.domain.music.application.dto.MusicAPICondition;
import org.sopt.confeti.domain.music.application.dto.PersistResult;
import org.sopt.confeti.global.common.redis.RedisHandler;
import org.sopt.confeti.global.common.redis.RedisKey;
import org.sopt.confeti.global.resolver.music_api.artist.vo.ConfetiArtist;
import org.sopt.confeti.global.resolver.music_api.song.vo.ConfetiSong;
import org.sopt.confeti.global.resolver.music_api.song.vo.ConfetiSongArtist;
import org.sopt.confeti.global.util.music.MusicAPIHandler;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TopArtistMusicAPIService extends MusicAPIService<ConfetiArtist> {

    private static final int FETCH_TOP_SONG_MAX_SIZE = 200;

    private final RedisHandler redisHandler;
    private final TopArtistService topArtistService;
    private final MusicAPIHandler musicAPIHandler;

    @Override
    protected void cache(List<ConfetiArtist> targetList) {
        redisHandler.set(RedisKey.MUSIC_TOP_ARTISTS.createKeyInfo(), targetList);
    }

    @Override
    protected void persist(List<ConfetiArtist> targetList) {
        topArtistService.create(targetList);
    }

    @Override
    protected CacheResult<ConfetiArtist> getCached(MusicAPICondition musicAPICondition) {
        List<ConfetiArtist> cachedTopArtists = redisHandler.getList(
            RedisKey.MUSIC_TOP_ARTISTS.createKeyInfo());
        return new CacheResult<>(cachedTopArtists, new HashSet<>());
    }

    @Override
    protected PersistResult<ConfetiArtist> getPersisted(MusicAPICondition musicAPICondition) {
        List<ConfetiArtist> persistedTopArtists = topArtistService.getTopArtists();

        cache(persistedTopArtists);

        return new PersistResult<>(persistedTopArtists, new HashSet<>());
    }

    @Override
    protected FetchResult<ConfetiArtist> getFetched(MusicAPICondition musicAPICondition) {
        List<ConfetiSong> topSongs = musicAPIHandler.getTopSongs(FETCH_TOP_SONG_MAX_SIZE);
        Set<String> topSongIds = topSongs.stream()
            .map(ConfetiSong::getId)
            .collect(Collectors.toSet());

        List<ConfetiSong> topSongsWithArtists = musicAPIHandler.getSongsBySongIds(topSongIds);
        Set<String> topArtistIds = topSongsWithArtists.stream()
            .flatMap(song -> song.getArtists().stream())
            .map(ConfetiSongArtist::getId)
            .collect(Collectors.toSet());

        List<ConfetiArtist> fetchedTopArtists = musicAPIHandler.getArtistsByArtistIds(topArtistIds);

        persist(fetchedTopArtists);

        return new FetchResult<>(fetchedTopArtists);
    }
}
