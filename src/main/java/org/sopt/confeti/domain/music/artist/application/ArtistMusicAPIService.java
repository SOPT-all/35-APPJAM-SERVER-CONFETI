package org.sopt.confeti.domain.music.artist.application;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.sopt.confeti.domain.music.application.MusicAPIService;
import org.sopt.confeti.domain.music.application.dto.CacheResult;
import org.sopt.confeti.domain.music.application.dto.FetchResult;
import org.sopt.confeti.domain.music.application.dto.MusicAPICondition;
import org.sopt.confeti.domain.music.application.dto.PersistResult;
import org.sopt.confeti.domain.music.artist.Artist;
import org.sopt.confeti.domain.music.artist.CreateArtistsEvent;
import org.sopt.confeti.global.common.redis.RedisHandler;
import org.sopt.confeti.global.common.redis.RedisHandler.RedisData;
import org.sopt.confeti.global.common.redis.RedisKey;
import org.sopt.confeti.global.common.redis.RedisKey.KeyInfo;
import org.sopt.confeti.global.resolver.music_api.artist.vo.ConfetiArtist;
import org.sopt.confeti.global.util.music.MusicAPIHandler;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ArtistMusicAPIService extends MusicAPIService<ConfetiArtist> {

    private final RedisHandler redisHandler;
    private final ArtistService artistService;
    private final MusicAPIHandler musicAPIHandler;
    private final ApplicationEventPublisher eventPublisher;

    @Override
    protected void cache(List<ConfetiArtist> targetList) {
        List<RedisData<ConfetiArtist>> dataList = targetList.stream()
            .map(target -> RedisData.<ConfetiArtist>builder()
                .keyInfo(RedisKey.MUSIC_ARTISTS.createKeyInfo(target.getId()))
                .value(target)
                .build())
            .toList();

        redisHandler.multiSet(dataList);
    }

    @Override
    protected void persist(List<ConfetiArtist> targetList) {
        eventPublisher.publishEvent(new CreateArtistsEvent(targetList));
    }

    @Override
    protected CacheResult<ConfetiArtist> getCached(MusicAPICondition musicAPICondition) {
        List<KeyInfo<ConfetiArtist>> keyInfos = musicAPICondition.ids().stream()
            .map(RedisKey.MUSIC_ARTISTS::<ConfetiArtist>createKeyInfo)
            .toList();
        List<ConfetiArtist> cachedArtists = redisHandler.multiGet(keyInfos);
        Set<String> cachedArtistIds = cachedArtists.stream()
            .map(ConfetiArtist::getId)
            .collect(Collectors.toSet());

        return new CacheResult<>(cachedArtists, cachedArtistIds);
    }

    @Override
    protected PersistResult<ConfetiArtist> getPersisted(MusicAPICondition musicAPICondition) {
        List<ConfetiArtist> persistedSongs = artistService.getArtists(musicAPICondition.ids())
            .stream()
            .map(Artist::toDomain)
            .toList();
        Set<String> persistedSongIds = persistedSongs.stream()
            .map(ConfetiArtist::getId)
            .collect(Collectors.toSet());

        cache(persistedSongs);

        return new PersistResult<>(persistedSongs, persistedSongIds);
    }

    @Override
    protected FetchResult<ConfetiArtist> getFetched(MusicAPICondition musicAPICondition) {
        List<ConfetiArtist> fetchedSongs = musicAPIHandler.getArtistsByArtistIds(
            musicAPICondition.ids());

        persist(fetchedSongs);

        return new FetchResult<>(fetchedSongs);
    }
}
