package org.sopt.confeti.domain.music.topartist.application;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.sopt.confeti.domain.music.application.MusicService;
import org.sopt.confeti.domain.music.application.dto.CacheResult;
import org.sopt.confeti.domain.music.application.dto.FetchResult;
import org.sopt.confeti.domain.music.application.dto.MusicCondition;
import org.sopt.confeti.domain.music.application.dto.PersistResult;
import org.sopt.confeti.global.common.redis.RedisHandler;
import org.sopt.confeti.global.common.redis.RedisKey;
import org.sopt.confeti.global.resolver.music_api.artist.vo.ConfetiArtist;
import org.sopt.confeti.global.resolver.music_api.music.vo.ConfetiMusic;
import org.sopt.confeti.global.resolver.music_api.music.vo.ConfetiMusicArtist;
import org.sopt.confeti.global.util.music.MusicAPIHandler;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TopArtistMusicService extends MusicService<ConfetiArtist> {

    private static final int FETCH_TOP_MUSIC_MAX_SIZE = 200;

    private final RedisHandler redisHandler;
    private final TopArtistService topArtistService;
    private final MusicAPIHandler musicAPIHandler;

    @Override
    public void cache(List<ConfetiArtist> targetList) {
        redisHandler.set(RedisKey.MUSIC_TOP_ARTISTS.createKeyInfo(), targetList);
    }

    @Override
    public void persist(List<ConfetiArtist> targetList) {
        topArtistService.create(targetList);
    }

    @Override
    public CacheResult<ConfetiArtist> getCached(MusicCondition musicCondition) {
        List<ConfetiArtist> cachedTopArtists = redisHandler.getList(RedisKey.MUSIC_TOP_ARTISTS.createKeyInfo());
        return new CacheResult<>(cachedTopArtists, new HashSet<>());
    }

    @Override
    public PersistResult<ConfetiArtist> getPersisted(MusicCondition musicCondition) {
        List<ConfetiArtist> persistedTopArtists = topArtistService.getTopArtists();

        return new PersistResult<>(persistedTopArtists, new HashSet<>());
    }

    @Override
    public FetchResult<ConfetiArtist> getFetched(MusicCondition musicCondition) {
        List<ConfetiMusic> topMusics = musicAPIHandler.getTopMusics(FETCH_TOP_MUSIC_MAX_SIZE);
        Set<String> topMusicIds = topMusics.stream()
                .map(ConfetiMusic::getId)
                .collect(Collectors.toSet());

        List<ConfetiMusic> topMusicsWithArtists = musicAPIHandler.getMusicsByMusicIds(topMusicIds);
        Set<String> topArtistIds = topMusicsWithArtists.stream()
                .flatMap(music -> music.getArtists().stream())
                .map(ConfetiMusicArtist::getId)
                .collect(Collectors.toSet());

        List<ConfetiArtist> fetchedTopArtists = musicAPIHandler.getArtistsByArtistIds(topArtistIds);

        cache(fetchedTopArtists);
        persist(fetchedTopArtists);

        return new FetchResult<>(fetchedTopArtists);
    }
}
