package org.sopt.confeti.global.util.music;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.sopt.confeti.external.client.AppleMusicFeignClient;
import org.sopt.confeti.global.annotation.Handler;
import org.sopt.confeti.global.exception.ConfetiException;
import org.sopt.confeti.global.message.ErrorMessage;
import org.sopt.confeti.global.resolver.music_api.artist.vo.ConfetiArtist;
import org.sopt.confeti.global.resolver.music_api.music.vo.ConfetiMusic;
import org.sopt.confeti.global.util.RedisHandler;
import org.sopt.confeti.global.util.RedisKey;
import org.sopt.confeti.global.util.music.dto.artist.AppleMusicArtistResponse;
import org.sopt.confeti.global.util.music.dto.artist.AppleMusicArtistsResponse;
import org.sopt.confeti.global.util.music.dto.chart.AppleMusicChartResponse;
import org.sopt.confeti.global.util.music.dto.chart.AppleMusicChartSongResponse;
import org.sopt.confeti.global.util.music.dto.chart.AppleMusicChartsResponse;
import org.sopt.confeti.global.util.music.dto.music.AppleMusicArtistMusicsResponse;
import org.sopt.confeti.global.util.music.dto.music.AppleMusicMusicResponse;
import org.sopt.confeti.global.util.music.dto.music.AppleMusicMusicsResponse;
import org.sopt.confeti.global.util.music.dto.music.MusicPage;
import org.sopt.confeti.global.util.music.dto.search.AppleMusicSearchResponse;
import org.sopt.confeti.global.util.music.dto.search.AppleMusicSearchResultsResponse;

@Slf4j
@Handler
@RequiredArgsConstructor
public class AppleMusicAPIHandler implements MusicAPIHandler {

    private static final String QUERY_PARAMETER_IDS_DELIMITER = ",";
    private static final String ARTISTS_TYPE = "artists";
    private static final String SONGS_TYPE = "songs";

    // Redis
    private static final int REDIS_TTL_DAY = 1;

    // Fetch Limit 목록
    private static final int CHARTS_FETCH_LIMIT = 200;
    private static final int ARTIST_TOP_SONGS_MULTIPLIER = 5;

    private final RedisHandler redisHandler;
    private final ObjectMapper objectMapper;

    private final AppleMusicFeignClient client;

    private List<ConfetiArtist> getCachedArtists(final Set<String> artistIds) {
        Object raw = redisHandler.multiGet(
                artistIds.stream()
                        .map(RedisKey.MUSIC_ARTISTS::get)
                        .collect(Collectors.toSet())
        );

        List<ConfetiArtist> cachedArtists = objectMapper.convertValue(raw, new TypeReference<List<ConfetiArtist>>() {
                }).stream()
                .filter(Objects::nonNull)
                .toList();

        Set<String> cachedArtistIds = cachedArtists.stream()
                .map(ConfetiArtist::getId)
                .collect(Collectors.toSet());

        artistIds.removeIf(cachedArtistIds::contains);

        return cachedArtists;
    }

    private void cachingArtists(final List<ConfetiArtist> artists) {
        artists.forEach(artist -> redisHandler.set(RedisKey.MUSIC_ARTISTS.get(artist.getId()), artist, REDIS_TTL_DAY, TimeUnit.DAYS));
    }

    @Override
    public List<ConfetiArtist> getArtistsByArtistIds(final Set<String> artistIds) {
        if (artistIds.isEmpty()) {
            return Collections.emptyList();
        }

        List<ConfetiArtist> artists = new ArrayList<>(getCachedArtists(artistIds));

        // Cache Hit된 아이디는 제거되므로 Apple Music API 서버 조회 전 목록 확인
        if (artistIds.isEmpty()) {
            return artists;
        }

        List<ConfetiArtist> fetchedArtists = convertToConfetiArtists(
                client.getArtists(String.join(QUERY_PARAMETER_IDS_DELIMITER, artistIds))
        );
        cachingArtists(fetchedArtists);
        artists.addAll(fetchedArtists);

        return artists;
    }

    private List<ConfetiArtist> getCachedRelatedArtists(final String artistId, final int limit) {
        Object raw = redisHandler.get(RedisKey.MUSIC_ARTISTS_RELATED.get(artistId, limit));
        if (Objects.isNull(raw)) {
            return List.of();
        }

        List<ConfetiArtist> artists = objectMapper.convertValue(raw, new TypeReference<>() {
        });

        return artists.stream()
                .limit(limit)
                .toList();
    }

    private void cacheRelatedArtists(final String artistId, final int limit, final List<ConfetiArtist> artists) {
        redisHandler.set(RedisKey.MUSIC_ARTISTS_RELATED.get(artistId, limit), artists, REDIS_TTL_DAY, TimeUnit.DAYS);
    }

    @Override
    public List<ConfetiArtist> getRelatedArtists(final String artistId, final int limit) {
        List<ConfetiArtist> artists = new ArrayList<>(getCachedRelatedArtists(artistId, limit));

        if (!artists.isEmpty()) {
            return artists;
        }

        List<ConfetiArtist> fetchedArtists = convertToConfetiArtists(
                client.getRelatedArtistsById(artistId, String.valueOf(limit))
        );
        cacheRelatedArtists(artistId, limit, fetchedArtists);
        artists.addAll(fetchedArtists);

        return artists;
    }

    @Override
    public Optional<ConfetiArtist> findArtistByKeyword(final String keyword) {
        return findArtistsByKeyword(keyword, 1).stream()
                .findFirst();
    }

    @Override
    public List<ConfetiArtist> findArtistsByKeyword(final String keyword, final int limit) {
        return convertToConfetiArtists(
                client.searchByKeyword(keyword, ARTISTS_TYPE, String.valueOf(limit), null, "topResults")
        );
    }

    private Optional<ConfetiArtist> getCachedArtist(final String artistId) {
        Object raw = redisHandler.get(RedisKey.MUSIC_ARTISTS.get(artistId));

        if (Objects.isNull(raw)) {
            return Optional.empty();
        }

        return Optional.of(objectMapper.convertValue(raw, ConfetiArtist.class));
    }

    private void cachingArtist(final ConfetiArtist artist) {
        redisHandler.set(RedisKey.MUSIC_ARTISTS.get(artist.getId()), artist, REDIS_TTL_DAY, TimeUnit.DAYS);
    }

    @Override
    public Optional<ConfetiArtist> findArtistByArtistId(final String artistId) {
        Optional<ConfetiArtist> cachedArtist = getCachedArtist(artistId);
        if (cachedArtist.isPresent()) {
            return cachedArtist;
        }

        Optional<ConfetiArtist> artist = convertToConfetiArtist(
                client.getArtistById(artistId)
        );
        artist.ifPresent(this::cachingArtist);

        return artist;
    }

    private List<ConfetiArtist> convertToConfetiArtists(final AppleMusicArtistsResponse artists) {
        return Optional.ofNullable(artists)
                .map(AppleMusicArtistsResponse::data)
                .map(data ->
                        data.stream()
                                .map(ConfetiArtist::from)
                                .toList()
                ).orElseGet(List::of);
    }

    private List<ConfetiArtist> convertToConfetiArtists(final AppleMusicSearchResponse searchResult) {
        return Optional.ofNullable(searchResult)
                .map(AppleMusicSearchResponse::results)
                .map(AppleMusicSearchResultsResponse::artists)
                .map(this::convertToConfetiArtists)
                .orElseGet(List::of);
    }

    private Optional<ConfetiArtist> convertToConfetiArtist(final AppleMusicArtistResponse artist) {
        return Optional.of(
                Optional.ofNullable(artist)
                    .map(ConfetiArtist::from)
                    .orElse(ConfetiArtist.empty())
        );
    }

    private List<ConfetiMusic> getCachedMusics(final Set<String> musicIds) {
        Object raw = redisHandler.multiGet(
                musicIds.stream()
                        .map(RedisKey.MUSIC_MUSICS::get)
                        .collect(Collectors.toSet())
        );

        List<ConfetiMusic> cachedMusics = objectMapper.convertValue(raw, new TypeReference<List<ConfetiMusic>>() {
                }).stream()
                .filter(Objects::nonNull)
                .toList();

        Set<String> cachedMusicIds = cachedMusics.stream()
                .map(ConfetiMusic::getId)
                .collect(Collectors.toSet());

        musicIds.removeIf(cachedMusicIds::contains);

        return cachedMusics;
    }

    private void cachingMusics(final List<ConfetiMusic> musics) {
        musics.forEach(music -> redisHandler.set(RedisKey.MUSIC_MUSICS.get(music.getId()), music, REDIS_TTL_DAY, TimeUnit.DAYS));
    }

    @Override
    public List<ConfetiMusic> getMusicsByMusicIds(final Set<String> musicIds) {
        if (musicIds.isEmpty()) {
            return Collections.emptyList();
        }

        List<ConfetiMusic> musics = new ArrayList<>(getCachedMusics(musicIds));

        // Cache Hit된 아이디는 제거되므로 Apple Music API 서버 조회 전 목록 확인
        if (musicIds.isEmpty()) {
            return musics;
        }

        List<ConfetiMusic> fetchedMusics = convertToConfetiMusics(
                client.getSongsByIds(String.join(QUERY_PARAMETER_IDS_DELIMITER, musicIds))
        );
        cachingMusics(fetchedMusics);
        musics.addAll(fetchedMusics);

        return musics;
    }

    private List<ConfetiMusic> convertToConfetiMusics(final AppleMusicMusicsResponse musics) {
        return Optional.ofNullable(musics)
                .map(AppleMusicMusicsResponse::data)
                .map(data ->
                        data.stream()
                                .map(ConfetiMusic::from)
                                .toList()
                ).orElseGet(List::of);
    }

    private List<ConfetiMusic> getCachedTopMusics(final int fetchSize) {
        Object raw = redisHandler.get(RedisKey.MUSIC_TOP_MUSICS.get());

        if (Objects.isNull(raw)) {
            return List.of();
        }

        List<ConfetiMusic> musics = objectMapper.convertValue(raw, new TypeReference<>() {
        });

        if (musics.size() < fetchSize) {
            return List.of();
        }

        return musics.stream()
                .limit(fetchSize)
                .toList();
    }

    private void cachingTopMusics(final List<ConfetiMusic> musics) {
        redisHandler.set(RedisKey.MUSIC_TOP_MUSICS.get(), musics, REDIS_TTL_DAY, TimeUnit.DAYS );
    }

    @Override
    public List<ConfetiMusic> getTopMusics(final int fetchSize) {
        validateFetchSize(fetchSize);

        List<ConfetiMusic> topMusics = new ArrayList<>(getCachedTopMusics(fetchSize));
        if (!topMusics.isEmpty()) {
            return topMusics;
        }

        topMusics.addAll(convertToConfetiMusics(
                client.getCharts(SONGS_TYPE, String.valueOf(fetchSize))
        ));
        cachingTopMusics(topMusics);

        return topMusics;
    }

    private List<ConfetiMusic> convertToConfetiMusics(final AppleMusicChartsResponse charts) {
        return Optional.ofNullable(charts)
                .map(AppleMusicChartsResponse::results)
                .map(AppleMusicChartResponse::songs)
                .map(songs ->
                        songs.stream()
                                .findFirst()
                                .map(AppleMusicChartSongResponse::data)
                                .orElseGet(List::of)
                )
                .map(musics ->
                        musics.stream()
                                .map(ConfetiMusic::from)
                                .toList()
                ).orElseGet(List::of);
    }

    private void validateFetchSize(int fetchSize) {
        if (fetchSize > CHARTS_FETCH_LIMIT) {
            throw new ConfetiException(ErrorMessage.BAD_REQUEST);
        }
    }

    private List<ConfetiMusic> getCachedTopMusicsByArtistId(final String artistId, final int fetchSize) {
        Object raw = redisHandler.get(RedisKey.MUSIC_ARTISTS_TOP_MUSICS.get(artistId));

        if (Objects.isNull(raw)) {
            return List.of();
        }

        List<ConfetiMusic> musics = objectMapper.convertValue(raw, new TypeReference<>() {
        });

        if (musics.size() < fetchSize) {
            return List.of();
        }

        return musics.stream()
                .limit(fetchSize)
                .toList();
    }

    private void cachingTopMusicsByArtistId(final String artistId, final List<ConfetiMusic> musics) {
        redisHandler.set(RedisKey.MUSIC_ARTISTS_TOP_MUSICS.get(artistId), musics, REDIS_TTL_DAY, TimeUnit.DAYS) ;
    }

    public List<ConfetiMusic> getTopSongsByArtistId(final String artistId, final int fetchSize) {
        List<ConfetiMusic> topMusics = new ArrayList<>(getCachedTopMusicsByArtistId(artistId, fetchSize));
        if (!topMusics.isEmpty()) {
            return topMusics;
        }

        topMusics.addAll(convertToConfetiMusics(
                client.getArtistTopSongsById(artistId, String.valueOf(fetchSize))
        ));
        cachingTopMusicsByArtistId(artistId, topMusics);

        return topMusics;
    }

    @Override
    public List<ConfetiMusic> getFilteredTopSongsByArtist(String artistId, int limit, Set<String> excludedMusicIds) {
        List<ConfetiMusic> songs = getTopSongsByArtistId(artistId, limit * ARTIST_TOP_SONGS_MULTIPLIER);

        List<ConfetiMusic> filteredSongs = songs.stream()
                .filter(song -> !excludedMusicIds.contains(song.getId()))
                .collect(Collectors.toList());

        if (filteredSongs.isEmpty()) {
            return Collections.emptyList();
        }

        List<ConfetiMusic> result = new ArrayList<>();
        Random random = new Random();

        for (int i = 0; i < limit && !filteredSongs.isEmpty(); i++) {
            int randomIndex = random.nextInt(filteredSongs.size());
            result.add(filteredSongs.get(randomIndex));
            filteredSongs.remove(randomIndex);
        }

        return result;
    }

    private Optional<MusicPage> getCachedMusicPageByArtistId(String artistId, int offset, int limit) {
        // check exists artist
        Object raw = redisHandler.get(RedisKey.MUSIC_PAGE_ARTIST_OFFSET_LIMIT.get(artistId, offset, limit));

        if (Objects.isNull(raw)) {
            return Optional.empty();
        }

        return Optional.of(objectMapper.convertValue(raw, MusicPage.class));
    }

    private void cachingMusicPageByArtistId(String artistId, int offset, int limit, MusicPage musicPage) {
        redisHandler.set(RedisKey.MUSIC_PAGE_ARTIST_OFFSET_LIMIT.get(artistId, offset, limit), musicPage, REDIS_TTL_DAY, TimeUnit.DAYS);
    }

    @Override
    public MusicPage getArtistMusicsByArtistId(String artistId, int offset, int limit) {
        Optional<MusicPage> optMusicPage = getCachedMusicPageByArtistId(artistId, offset, limit);
        if (optMusicPage.isPresent()) {
            return optMusicPage.get();
        }

        MusicPage musicPage = convertToConfetiMusicPage(
                client.getArtistSongsById(artistId, String.valueOf(limit), String.valueOf(offset))
        );
        cachingMusicPageByArtistId(artistId, offset, limit, musicPage);

        return musicPage;
    }

    private MusicPage convertToConfetiMusicPage(AppleMusicArtistMusicsResponse artistMusics) {
        return Optional.ofNullable(artistMusics)
                .map(musics -> MusicPage.of(
                        musics.next(),
                        Optional.ofNullable(musics.data())
                                .map(data ->
                                        data.stream()
                                                .map(ConfetiMusic::from)
                                                .toList()
                                ).orElseGet(List::of)
                )).orElseGet(MusicPage::empty);
    }

    private Optional<MusicPage> getCachedMusicPageByKeyword(String term, int offset, int limit) {
        Object raw = redisHandler.get(RedisKey.MUSIC_PAGE_KEYWORD_OFFSET_LIMIT.get(term, offset, limit));

        if (Objects.isNull(raw)) {
            return Optional.empty();
        }

        return Optional.of(objectMapper.convertValue(raw, MusicPage.class));
    }

    private void cachingMusicPageByKeyword(String term, int offset, int limit, MusicPage musicPage) {
        redisHandler.set(RedisKey.MUSIC_PAGE_KEYWORD_OFFSET_LIMIT.get(term, offset, limit), musicPage, REDIS_TTL_DAY, TimeUnit.DAYS);
    }

    @Override
    public MusicPage getMusicsByKeyword(String term, int offset, int limit) {
        Optional<MusicPage> optMusicPage = getCachedMusicPageByKeyword(term, offset, limit);
        if (optMusicPage.isPresent()) {
            return optMusicPage.get();
        }

        MusicPage musicPage = convertToConfetiMusicPage(
                client.searchByKeyword(term, SONGS_TYPE, String.valueOf(limit), String.valueOf(offset), null)
        );
        cachingMusicPageByKeyword(term, offset, limit, musicPage);

        return musicPage;
    }

    private MusicPage convertToConfetiMusicPage(AppleMusicSearchResponse searchResult) {
        return Optional.ofNullable(searchResult)
                .map(AppleMusicSearchResponse::results)
                .map(AppleMusicSearchResultsResponse::songs)
                .map(musics -> MusicPage.of(
                        musics.next(),
                        Optional.ofNullable(musics.data())
                                .map(data ->
                                        data.stream()
                                                .map(ConfetiMusic::from)
                                                .toList()
                                ).orElseGet(List::of)
                )).orElseGet(MusicPage::empty);
    }
}