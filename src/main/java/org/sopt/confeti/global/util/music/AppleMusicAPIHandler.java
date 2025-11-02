package org.sopt.confeti.global.util.music;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.sopt.confeti.external.client.AppleMusicFeignClient;
import org.sopt.confeti.global.annotation.Handler;
import org.sopt.confeti.global.common.redis.RedisHandler.RedisData;
import org.sopt.confeti.global.common.redis.RedisKey;
import org.sopt.confeti.global.common.redis.RedisKey.KeyInfo;
import org.sopt.confeti.global.exception.ConfetiException;
import org.sopt.confeti.global.message.ErrorMessage;
import org.sopt.confeti.global.resolver.music_api.artist.vo.ConfetiArtist;
import org.sopt.confeti.global.resolver.music_api.music.vo.ConfetiMusic;
import org.sopt.confeti.global.common.redis.RedisHandler;
import org.sopt.confeti.global.util.music.dto.music.MusicPage;

@Slf4j
@Handler
@RequiredArgsConstructor
public class AppleMusicAPIHandler implements MusicAPIHandler {

    private static final String QUERY_PARAMETER_IDS_DELIMITER = ",";
    private static final String ARTISTS_TYPE = "artists";
    private static final String SONGS_TYPE = "songs";

    // Fetch Limit 목록
    private static final int CHARTS_FETCH_LIMIT = 200;
    private static final int ARTIST_TOP_SONGS_MULTIPLIER = 5;

    private final RedisHandler redisHandler;
    private final AppleMusicFeignClient client;
    private final AppleMusicAPIResponseConverter responseConverter;

    @Override
    public List<ConfetiArtist> getArtistsByArtistIds(final Set<String> artistIds) {
        if (artistIds.isEmpty()) {
            return Collections.emptyList();
        }

        // 1. 캐시 조회
        List<ConfetiArtist> cachedArtists = getCachedArtists(artistIds);

        if (cachedArtists.size() == artistIds.size()) {
            // 1.1 더 이상 조회할 아이디가 없을 경우 반환
            return cachedArtists;
        }

        Set<String> cachedArtistIds = cachedArtists.stream()
                .map(ConfetiArtist::getId)
                .collect(Collectors.toSet());

        // 2. 조회 대상 아이디 추출
        Set<String> uncachedArtistIds = artistIds.stream()
                .filter(artistId -> !cachedArtistIds.contains(artistId))
                .collect(Collectors.toSet());

        // 3. Apple Music에서 조회
        List<ConfetiArtist> fetchedArtists = responseConverter.convertToConfetiArtists(
                client.getArtists(String.join(QUERY_PARAMETER_IDS_DELIMITER, uncachedArtistIds))
        );
        // 4. 캐시 업데이트
        cachingArtists(fetchedArtists);

        return Stream.concat(cachedArtists.stream(), fetchedArtists.stream()).toList();
    }

    private List<ConfetiArtist> getCachedArtists(Set<String> artistIds) {
        List<KeyInfo> keyInfos = artistIds.stream()
                .map(RedisKey.MUSIC_ARTISTS::createKeyInfo)
                .toList();

        return redisHandler.multiGet(keyInfos);
    }

    private void cachingArtists(final List<ConfetiArtist> artists) {
        List<RedisData<ConfetiArtist>> dataList = artists.stream()
                .map(artist ->
                        RedisData.<ConfetiArtist>builder()
                                .keyInfo(RedisKey.MUSIC_ARTISTS.createKeyInfo(artist.getId()))
                                .value(artist)
                                .build()
                ).toList();

        redisHandler.multiSet(dataList);
    }

    @Override
    public List<ConfetiArtist> getRelatedArtists(final String artistId, final int limit) {
        // 1. 캐시 조회
        List<ConfetiArtist> cachedArtists = getCachedRelatedArtists(artistId, limit);

        if (!cachedArtists.isEmpty()) {
            // 1.1 캐시 히트 시 반환
            return cachedArtists;
        }

        // 2. Apple Music 조회
        List<ConfetiArtist> fetchedArtists = responseConverter.convertToConfetiArtists(
                client.getRelatedArtistsById(artistId, String.valueOf(limit))
        );
        // 3. 캐시 업데이트
        cachingRelatedArtists(artistId, limit, fetchedArtists);

        return fetchedArtists;
    }

    private List<ConfetiArtist> getCachedRelatedArtists(String artistId, int limit) {
        return redisHandler.getList(RedisKey.MUSIC_ARTISTS_RELATED.createKeyInfo(artistId, limit));
    }

    private void cachingRelatedArtists(String artistId, int limit, List<ConfetiArtist> fetchedArtists) {
        redisHandler.set(RedisKey.MUSIC_ARTISTS_RELATED.createKeyInfo(artistId, limit), fetchedArtists);
    }

    @Override
    public Optional<ConfetiArtist> findArtistByKeyword(final String keyword) {
        return findArtistsByKeyword(keyword, 1).stream()
                .findFirst();
    }

    @Override
    public List<ConfetiArtist> findArtistsByKeyword(final String keyword, final int limit) {
        return responseConverter.convertToConfetiArtists(
                client.searchByKeyword(keyword, ARTISTS_TYPE, String.valueOf(limit), null, "topResults")
        );
    }

    @Override
    public Optional<ConfetiArtist> findArtistByArtistId(final String artistId) {
        Optional<ConfetiArtist> cachedArtist = getCachedArtist(artistId);
        if (cachedArtist.isPresent()) {
            return cachedArtist;
        }

        Optional<ConfetiArtist> artist = responseConverter.convertToConfetiArtist(
                client.getArtistById(artistId)
        );
        artist.ifPresent(this::cachingArtist);

        return artist;
    }

    private Optional<ConfetiArtist> getCachedArtist(final String artistId) {
        return redisHandler.get(RedisKey.MUSIC_ARTISTS.createKeyInfo(artistId));
    }

    private void cachingArtist(final ConfetiArtist artist) {
        redisHandler.set(RedisKey.MUSIC_ARTISTS.createKeyInfo(artist.getId()), artist);
    }

    @Override
    public List<ConfetiMusic> getMusicsByMusicIds(final Set<String> musicIds) {
        if (musicIds.isEmpty()) {
            return Collections.emptyList();
        }

        // 1. 캐시 조회
        List<ConfetiMusic> cachedMusics = getCachedMusics(musicIds);
        if (cachedMusics.size() == musicIds.size()) {
            // 1.1 더 이상 조회할 아이디가 없을 경우 반환
            return cachedMusics;
        }

        Set<String> cachedMusicIds = cachedMusics.stream()
                .map(ConfetiMusic::getId)
                .collect(Collectors.toSet());

        // 2. 조회 대상 아이디 추출
        Set<String> uncachedMusicIds = musicIds.stream()
                .filter(musicId -> !cachedMusicIds.contains(musicId))
                .collect(Collectors.toSet());

        // 3. Apple Music에서 조회
        List<ConfetiMusic> fetchedMusics = responseConverter.convertToConfetiMusics(
                client.getSongsByIds(String.join(QUERY_PARAMETER_IDS_DELIMITER, uncachedMusicIds))
        );
        // 4. 캐시 업데이트
        cachingMusics(fetchedMusics);

        return Stream.concat(cachedMusics.stream(), fetchedMusics.stream()).toList();
    }

    private List<ConfetiMusic> getCachedMusics(Set<String> musicIds) {
        List<KeyInfo> keyInfos = musicIds.stream()
                .map(RedisKey.MUSIC_MUSICS::createKeyInfo)
                .toList();

        return redisHandler.multiGet(keyInfos);
    }

    private void cachingMusics(List<ConfetiMusic> musics) {
        List<RedisData<ConfetiMusic>> dataList = musics.stream()
                .map(music ->
                        RedisData.<ConfetiMusic>builder()
                                .keyInfo(RedisKey.MUSIC_MUSICS.createKeyInfo(music.getId()))
                                .value(music)
                                .build()
                ).toList();

        redisHandler.multiSet(dataList);
    }

    @Override
    public List<ConfetiMusic> getTopMusics(final int fetchSize) {
        validateFetchSize(fetchSize);

        List<ConfetiMusic> cachedTopMusics = getCachedTopMusics(fetchSize);
        if (!cachedTopMusics.isEmpty()) {
            return cachedTopMusics;
        }

        List<ConfetiMusic> fetchedTopMusics = responseConverter.convertToConfetiMusics(
                client.getCharts(SONGS_TYPE, String.valueOf(fetchSize))
        );
        cachingTopMusics(fetchedTopMusics);

        return fetchedTopMusics;
    }

    private List<ConfetiMusic> getCachedTopMusics(final int fetchSize) {
        return redisHandler.<ConfetiMusic>getList(RedisKey.MUSIC_TOP_MUSICS.createKeyInfo()).stream()
                .limit(fetchSize)
                .toList();
    }

    private void cachingTopMusics(final List<ConfetiMusic> fetchedTopMusics) {
        redisHandler.set(RedisKey.MUSIC_TOP_MUSICS.createKeyInfo(), fetchedTopMusics);
    }

    private void validateFetchSize(int fetchSize) {
        if (fetchSize > CHARTS_FETCH_LIMIT) {
            throw new ConfetiException(ErrorMessage.BAD_REQUEST);
        }
    }

    @Deprecated
    @Override
    public List<ConfetiMusic> getFilteredTopSongsByArtist(String artistId, int limit, Set<String> excludedMusicIds) {
        log.debug("Get filtered top songs. Artist id : {}, limit : {}, excludeMusicIds : {}", artistId, limit, excludedMusicIds);
        List<ConfetiMusic> songs = getTopSongsByArtistId(artistId, limit * ARTIST_TOP_SONGS_MULTIPLIER);

        log.debug("Filter");
        List<ConfetiMusic> filteredSongs = songs.stream()
                .filter(song -> !excludedMusicIds.contains(song.getId()))
                .collect(Collectors.toList());

        if (filteredSongs.isEmpty()) {
            log.debug("Empty");
            return Collections.emptyList();
        }

        List<ConfetiMusic> result = new ArrayList<>();
        Random random = new Random();

        log.debug("Pick random");
        for (int i = 0; i < limit && !filteredSongs.isEmpty(); i++) {
            int randomIndex = random.nextInt(filteredSongs.size());
            result.add(filteredSongs.get(randomIndex));
            filteredSongs.remove(randomIndex);
        }
        log.debug("End");

        return result;
    }

    public List<ConfetiMusic> getTopSongsByArtistId(final String artistId, final int fetchSize) {
        log.debug("Try to get cached top musics. Artist id : {}, fetch size : {}", artistId, fetchSize);
        List<ConfetiMusic> cachedTopMusics = getCachedTopMusicsByArtistId(artistId, fetchSize);
        if (!cachedTopMusics.isEmpty()) {
            log.debug("cache hit");
            return cachedTopMusics;
        }

        log.debug("Try to get fetched top musics. Artist id : {}, fetch size : {}", artistId, fetchSize);
        List<ConfetiMusic> fetchedTopMusics = responseConverter.convertToConfetiMusics(
                client.getArtistTopSongsById(artistId, String.valueOf(fetchSize))
        );
        log.debug("caching : {}", fetchedTopMusics);
        cachingTopMusicsByArtistId(artistId, fetchedTopMusics);

        return fetchedTopMusics;
    }

    private List<ConfetiMusic> getCachedTopMusicsByArtistId(final String artistId, final int fetchSize) {
        List<ConfetiMusic> musics = redisHandler.getList(RedisKey.MUSIC_ARTISTS_TOP_MUSICS.createKeyInfo(artistId));

        if (musics.size() < fetchSize) {
            return List.of();
        }

        return musics.stream()
                .limit(fetchSize)
                .toList();
    }

    private void cachingTopMusicsByArtistId(final String artistId, final List<ConfetiMusic> musics) {
        redisHandler.set(RedisKey.MUSIC_ARTISTS_TOP_MUSICS.createKeyInfo(artistId), musics);
    }

    @Override
    public MusicPage getArtistMusicsByArtistId(String artistId, int offset, int limit) {
        Optional<MusicPage> optMusicPage = getCachedMusicPageByArtistId(artistId, offset, limit);
        if (optMusicPage.isPresent()) {
            return optMusicPage.get();
        }

        MusicPage musicPage = responseConverter.convertToConfetiMusicPage(
                client.getArtistSongsById(artistId, String.valueOf(limit), String.valueOf(offset))
        );
        cachingMusicPageByArtistId(artistId, offset, limit, musicPage);

        return musicPage;
    }

    private Optional<MusicPage> getCachedMusicPageByArtistId(String artistId, int offset, int limit) {
        return redisHandler.get(RedisKey.MUSIC_PAGE_ARTIST_OFFSET_LIMIT.createKeyInfo(artistId, offset, limit));
    }

    private void cachingMusicPageByArtistId(String artistId, int offset, int limit, MusicPage musicPage) {
        redisHandler.set(RedisKey.MUSIC_PAGE_ARTIST_OFFSET_LIMIT.createKeyInfo(artistId, offset ,limit), musicPage);
    }

    @Override
    public MusicPage getMusicsByKeyword(String term, int offset, int limit) {
        Optional<MusicPage> optMusicPage = getCachedMusicPageByKeyword(term, offset, limit);
        if (optMusicPage.isPresent()) {
            return optMusicPage.get();
        }

        MusicPage musicPage = responseConverter.convertToConfetiMusicPage(
                client.searchByKeyword(term, SONGS_TYPE, String.valueOf(limit), String.valueOf(offset), null)
        );
        cachingMusicPageByKeyword(term, offset, limit, musicPage);

        return musicPage;
    }

    private Optional<MusicPage> getCachedMusicPageByKeyword(String term, int offset, int limit) {
        return redisHandler.get(RedisKey.MUSIC_PAGE_KEYWORD_OFFSET_LIMIT.createKeyInfo(term, offset, limit));
    }

    private void cachingMusicPageByKeyword(String term, int offset, int limit, MusicPage musicPage) {
        redisHandler.set(RedisKey.MUSIC_PAGE_KEYWORD_OFFSET_LIMIT.createKeyInfo(term, offset, limit), musicPage);
    }

    @Override
    public List<ConfetiMusic> getArtistTopSongs(String artistId, int recommendSongFetchSize) {
        List<ConfetiMusic> songs = getCachedTopMusicsByArtistId(artistId, recommendSongFetchSize);

        if (!songs.isEmpty()) {
            return songs;
        }

        List<ConfetiMusic> fetchedSongs = responseConverter.convertToConfetiMusics(
                client.getArtistTopSongsById(artistId, String.valueOf(recommendSongFetchSize))
        );
        cachingTopMusicsByArtistId(artistId, fetchedSongs);

        return fetchedSongs;
    }
}