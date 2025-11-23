package org.sopt.confeti.domain.music.artist.application;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.sopt.confeti.domain.music.application.MusicService;
import org.sopt.confeti.domain.music.application.dto.CacheResult;
import org.sopt.confeti.domain.music.application.dto.FetchResult;
import org.sopt.confeti.domain.music.application.dto.MusicCondition;
import org.sopt.confeti.domain.music.application.dto.PersistResult;
import org.sopt.confeti.domain.music.artist.Artist;
import org.sopt.confeti.global.common.redis.RedisHandler;
import org.sopt.confeti.global.common.redis.RedisHandler.RedisData;
import org.sopt.confeti.global.common.redis.RedisKey;
import org.sopt.confeti.global.common.redis.RedisKey.KeyInfo;
import org.sopt.confeti.global.resolver.music_api.artist.vo.ConfetiArtist;
import org.sopt.confeti.global.util.music.MusicAPIHandler;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ArtistMusicService extends MusicService<ConfetiArtist> {

    private final RedisHandler redisHandler;
    private final ArtistService artistService;
    private final MusicAPIHandler musicAPIHandler;

    @Override
    public void cache(List<ConfetiArtist> targetList) {
        List<RedisData<ConfetiArtist>> dataList = targetList.stream()
            .map(target -> RedisData.<ConfetiArtist>builder()
                .keyInfo(RedisKey.MUSIC_ARTISTS.createKeyInfo(target.getId()))
                .value(target)
                .build())
            .toList();

        redisHandler.multiSet(dataList);
    }

    @Override
    public void persist(List<ConfetiArtist> targetList) {
        artistService.create(targetList);
    }

    @Override
    public CacheResult<ConfetiArtist> getCached(MusicCondition musicCondition) {
        List<KeyInfo<ConfetiArtist>> keyInfos = musicCondition.ids().stream()
            .map(RedisKey.MUSIC_ARTISTS::<ConfetiArtist>createKeyInfo)
            .toList();
        List<ConfetiArtist> cachedMusics = redisHandler.multiGet(keyInfos);
        Set<String> cachedMusicIds = cachedMusics.stream()
            .map(ConfetiArtist::getId)
            .collect(Collectors.toSet());

        return new CacheResult<>(cachedMusics, cachedMusicIds);
    }

    @Override
    public PersistResult<ConfetiArtist> getPersisted(MusicCondition musicCondition) {
        List<ConfetiArtist> persistedSongs = artistService.getArtists(musicCondition.ids()).stream()
            .map(Artist::toConfetiArtist)
            .toList();
        Set<String> persistedSongIds = persistedSongs.stream()
            .map(ConfetiArtist::getId)
            .collect(Collectors.toSet());

        cache(persistedSongs);

        return new PersistResult<>(persistedSongs, persistedSongIds);
    }

    @Override
    public FetchResult<ConfetiArtist> getFetched(MusicCondition musicCondition) {
        List<ConfetiArtist> fetchedSongs = musicAPIHandler.getArtistsByArtistIds(
            musicCondition.ids());

        cache(fetchedSongs);
        persist(fetchedSongs);

        return new FetchResult<>(fetchedSongs);
    }
}
