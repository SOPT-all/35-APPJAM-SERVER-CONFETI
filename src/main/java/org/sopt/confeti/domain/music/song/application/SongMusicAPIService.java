package org.sopt.confeti.domain.music.song.application;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.sopt.confeti.domain.music.application.MusicAPIService;
import org.sopt.confeti.domain.music.application.dto.CacheResult;
import org.sopt.confeti.domain.music.application.dto.FetchResult;
import org.sopt.confeti.domain.music.application.dto.MusicAPICondition;
import org.sopt.confeti.domain.music.application.dto.PersistResult;
import org.sopt.confeti.domain.music.song.CreateSongsEvent;
import org.sopt.confeti.domain.music.song.Song;
import org.sopt.confeti.global.common.redis.RedisHandler;
import org.sopt.confeti.global.common.redis.RedisHandler.RedisData;
import org.sopt.confeti.global.common.redis.RedisKey;
import org.sopt.confeti.global.common.redis.RedisKey.KeyInfo;
import org.sopt.confeti.global.resolver.music_api.song.vo.ConfetiSong;
import org.sopt.confeti.global.util.music.MusicAPIHandler;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class SongMusicAPIService extends MusicAPIService<ConfetiSong> {

    private final RedisHandler redisHandler;
    private final SongService songService;
    private final MusicAPIHandler musicAPIHandler;
    private final ApplicationEventPublisher eventPublisher;

    @Override
    protected void cache(List<ConfetiSong> targetList) {
        List<RedisData<ConfetiSong>> dataList = targetList.stream()
            .map(target -> RedisData.<ConfetiSong>builder()
                .keyInfo(RedisKey.MUSIC_SONGS.createKeyInfo(target.getId()))
                .value(target)
                .build())
            .toList();

        redisHandler.multiSet(dataList);
    }

    @Override
    protected void persist(List<ConfetiSong> targetList) {
        eventPublisher.publishEvent(new CreateSongsEvent(targetList));
    }

    @Override
    protected CacheResult<ConfetiSong> getCached(MusicAPICondition musicAPICondition) {
        List<KeyInfo<ConfetiSong>> keyInfos = musicAPICondition.ids().stream()
            .map(RedisKey.MUSIC_SONGS::<ConfetiSong>createKeyInfo)
            .toList();
        List<ConfetiSong> cachedSongs = redisHandler.multiGet(keyInfos);
        Set<String> cachedSongIds = cachedSongs.stream()
            .map(ConfetiSong::getId)
            .collect(Collectors.toSet());

        return new CacheResult<>(cachedSongs, cachedSongIds);
    }

    @Override
    protected PersistResult<ConfetiSong> getPersisted(MusicAPICondition musicAPICondition) {
        List<ConfetiSong> persistedSongs = songService.getSongs(musicAPICondition.ids()).stream()
            .map(Song::toConfetiSong)
            .toList();
        Set<String> persistedSongIds = persistedSongs.stream()
            .map(ConfetiSong::getId)
            .collect(Collectors.toSet());

        cache(persistedSongs);

        return new PersistResult<>(persistedSongs, persistedSongIds);
    }

    @Override
    protected FetchResult<ConfetiSong> getFetched(MusicAPICondition musicAPICondition) {
        List<ConfetiSong> fetchedSongs = musicAPIHandler.getSongsBySongIds(musicAPICondition.ids());

        persist(fetchedSongs);

        return new FetchResult<>(fetchedSongs);
    }

    public List<ConfetiSong> getPopularSongsByArtist(String artistId, int limit) {
        List<ConfetiSong> songs = safeGetArtistTopSongs(artistId, limit);
        if (songs.isEmpty()) {
            return List.of();
        }

        safePersistAndCacheMissingSongs(artistId, songs);
        return songs;
    }

    private List<ConfetiSong> safeGetArtistTopSongs(String artistId, int limit) {
        try {
            List<ConfetiSong> songs = musicAPIHandler.getArtistTopSongs(artistId, limit);
            if (songs == null || songs.isEmpty()) {
                return List.of();
            }
            return songs;
        } catch (Exception ex) {
            log.warn("SongMusicAPIService.getPopularSongsByArtist failed. artistId: {}, message: {}",
                artistId, ex.getMessage());
            return List.of();
        }
    }

    private void safePersistAndCacheMissingSongs(String artistId, List<ConfetiSong> songs) {
        try {
            Set<String> songIds = songs.stream()
                .map(ConfetiSong::getId)
                .collect(Collectors.toSet());
            CacheResult<ConfetiSong> cacheResult = getCached(MusicAPICondition.from(songIds));
            Set<String> cachedSongIds = cacheResult.cachedIds();
            if (cachedSongIds.size() == songIds.size()) {
                return;
            }

            List<ConfetiSong> missingSongs = songs.stream()
                .filter(song -> !cachedSongIds.contains(song.getId()))
                .toList();
            if (missingSongs.isEmpty()) {
                return;
            }

            persist(missingSongs);
            cache(missingSongs);
        } catch (Exception ex) {
            log.warn("SongMusicAPIService.getPopularSongsByArtist cache/persist failed. artistId: {}, message: {}",
                artistId, ex.getMessage());
        }
    }
}
