package org.sopt.confeti.domain.music.song.application;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.sopt.confeti.domain.music.application.MusicService;
import org.sopt.confeti.domain.music.application.dto.CacheResult;
import org.sopt.confeti.domain.music.application.dto.FetchResult;
import org.sopt.confeti.domain.music.application.dto.MusicCondition;
import org.sopt.confeti.domain.music.application.dto.PersistResult;
import org.sopt.confeti.domain.music.song.Song;
import org.sopt.confeti.global.common.redis.RedisHandler;
import org.sopt.confeti.global.common.redis.RedisHandler.RedisData;
import org.sopt.confeti.global.common.redis.RedisKey;
import org.sopt.confeti.global.common.redis.RedisKey.KeyInfo;
import org.sopt.confeti.global.resolver.music_api.music.vo.ConfetiMusic;
import org.sopt.confeti.global.util.music.MusicAPIHandler;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SongMusicService extends MusicService<ConfetiMusic> {

    private final RedisHandler redisHandler;
    private final SongService songService;
    private final MusicAPIHandler musicAPIHandler;

    @Override
    protected void cache(List<ConfetiMusic> targetList) {
        List<RedisData<ConfetiMusic>> dataList = targetList.stream()
            .map(target -> RedisData.<ConfetiMusic>builder()
                .keyInfo(RedisKey.MUSIC_MUSICS.createKeyInfo(target.getId()))
                .value(target)
                .build())
            .toList();

        redisHandler.multiSet(dataList);
    }

    @Override
    protected void persist(List<ConfetiMusic> targetList) {
        songService.create(targetList);
    }

    @Override
    protected CacheResult<ConfetiMusic> getCached(MusicCondition musicCondition) {
        List<KeyInfo<ConfetiMusic>> keyInfos = musicCondition.ids().stream()
            .map(RedisKey.MUSIC_MUSICS::<ConfetiMusic>createKeyInfo)
            .toList();
        List<ConfetiMusic> cachedMusics = redisHandler.multiGet(keyInfos);
        Set<String> cachedMusicIds = cachedMusics.stream()
            .map(ConfetiMusic::getId)
            .collect(Collectors.toSet());

        return new CacheResult<>(cachedMusics, cachedMusicIds);
    }

    @Override
    protected PersistResult<ConfetiMusic> getPersisted(MusicCondition musicCondition) {
        List<ConfetiMusic> persistedSongs = songService.getSongs(musicCondition.ids()).stream()
            .map(Song::toConfetiMusic)
            .toList();
        Set<String> persistedSongIds = persistedSongs.stream()
            .map(ConfetiMusic::getId)
            .collect(Collectors.toSet());

        cache(persistedSongs);

        return new PersistResult<>(persistedSongs, persistedSongIds);
    }

    @Override
    protected FetchResult<ConfetiMusic> getFetched(MusicCondition musicCondition) {
        List<ConfetiMusic> fetchedSongs = musicAPIHandler.getMusicsByMusicIds(musicCondition.ids());

        persist(fetchedSongs);
        cache(fetchedSongs);

        return new FetchResult<>(fetchedSongs);
    }
}
