package org.sopt.confeti.global.util.music;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.sopt.confeti.global.annotation.Handler;
import org.sopt.confeti.global.annotation.RetryOnTokenExpire;
import org.sopt.confeti.global.exception.ConfetiException;
import org.sopt.confeti.global.message.ErrorMessage;
import org.sopt.confeti.global.module.rest_client.builder.ApiRestClientBuilder;
import org.sopt.confeti.global.resolver.music_api.album.vo.ConfetiAlbum;
import org.sopt.confeti.global.resolver.music_api.artist.vo.ConfetiArtist;
import org.sopt.confeti.global.resolver.music_api.music.vo.ConfetiMusic;
import org.sopt.confeti.global.util.music.dto.album.AppleMusicAlbumsResponse;
import org.sopt.confeti.global.util.music.dto.artist.AppleMusicArtistsResponse;
import org.sopt.confeti.global.util.music.dto.chart.AppleMusicChartSongResponse;
import org.sopt.confeti.global.util.music.dto.chart.AppleMusicChartsResponse;
import org.sopt.confeti.global.util.music.dto.music.AppleMusicArtistMusicsResponse;
import org.sopt.confeti.global.util.music.dto.music.AppleMusicMusicResponse;
import org.sopt.confeti.global.util.music.dto.music.AppleMusicMusicsResponse;
import org.sopt.confeti.global.util.music.dto.music.MusicPage;
import org.sopt.confeti.global.util.music.dto.search.AppleMusicSearchResponse;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.util.MultiValueMap;

@Slf4j
@Handler
@RequiredArgsConstructor
public class AppleMusicAPIHandler implements MusicAPIHandler {

    private static final String QUERY_PARAMETER_IDS_DELIMITER = ",";
    private static final String ARTISTS_TYPE = "artists";
    private static final String SONGS_TYPE = "songs";

    private static final String ARTISTS_RELATIONSHIP_SIMILAR_VIEW = "similar-artists";

    // Redis
    private static final int REDIS_TTL_DAY = 1;
    private static final String REDIS_KEY_BASE = "apple-music-api:";
    private static final String REDIS_KEY_ARTISTS = REDIS_KEY_BASE + "artists:";
    private static final String REDIS_KEY_ARTISTS_RELATED = REDIS_KEY_BASE + "artists-related:%s:%d";
    private static final String REDIS_KEY_ARTISTS_TOP_MUSICS = REDIS_KEY_BASE + "artists:top-musics:";
    private static final String REDIS_KEY_MUSICS = REDIS_KEY_BASE + "musics:";
    private static final String REDIS_KEY_TOP_MUSICS = REDIS_KEY_BASE + "top-musics";
    private static final String REDIS_KEY_MUSIC_PAGE_ARTIST_OFFSET_LIMIT =
            REDIS_KEY_BASE + "music-page:artists:%s:%d:%d";
    private static final String REDIS_KEY_MUSIC_PAGE_KEYWORD_OFFSET_LIMIT =
            REDIS_KEY_BASE + "music-page:keyword:%s:%d:%d";
    private static final String REDIS_KEY_ALBUMS = REDIS_KEY_BASE + "albums:";

    // Fetch Limit 목록
    private static final int ARTISTS_FETCH_LIMIT = 25;
    private static final int ALBUMS_FETCH_LIMIT = 100;
    private static final int MUSICS_FETCH_LIMIT = 300;
    private static final int CHARTS_FETCH_LIMIT = 200;

    private final AppleMusicAPITokenGenerator tokenGenerator;
    private final AppleMusicAPIURL appleMusicAPIURL;
    private final ApiRestClientBuilder restClient;
    private final RedisTemplate<String, Object> redisTemplate;
    private final ObjectMapper objectMapper;

    private final Map<String, String> headers = new ConcurrentHashMap<>();
    private String accessToken;

    @PostConstruct
    private void init() {
        generateToken();
    }

    private void generateToken() {
        accessToken = tokenGenerator.generateToken();
        headers.put(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken);
    }

    private void refreshToken() {
        generateToken();
    }

    private List<ConfetiArtist> getCachedArtists(final Set<String> artistIds) {
        Object raw = redisTemplate.opsForValue().multiGet(
                artistIds.stream()
                        .map(artistId -> REDIS_KEY_ARTISTS + artistId)
                        .collect(Collectors.toSet())
        );

        List<ConfetiArtist> cachedArtists = objectMapper.convertValue(raw, new TypeReference<List<ConfetiArtist>>() {
                }).stream()
                .filter(Objects::nonNull)
                .toList();

        Set<String> cachedArtistIds = cachedArtists.stream()
                .map(ConfetiArtist::getId)
                .collect(Collectors.toSet());
        ;

        artistIds.removeIf(cachedArtistIds::contains);

        return cachedArtists;
    }

    private void cachingArtists(final List<ConfetiArtist> artists) {
        artists.forEach(artist -> {
            redisTemplate.opsForValue().set(REDIS_KEY_ARTISTS + artist.getId(), artist, REDIS_TTL_DAY, TimeUnit.DAYS);
        });
    }

    @Override
    @RetryOnTokenExpire
    public List<ConfetiArtist> getArtistsByArtistIds(final Set<String> artistIds) {
        if (artistIds.isEmpty()) {
            return Collections.emptyList();
        }

        List<ConfetiArtist> artists = new ArrayList<>(getCachedArtists(artistIds));

        AtomicInteger counter = new AtomicInteger();
        List<ConfetiArtist> artistResults = artistIds.stream()
                .collect(Collectors.groupingBy(artistId -> counter.getAndIncrement() / ARTISTS_FETCH_LIMIT))
                .values().parallelStream()
                .map(this::getArtistsByArtistIds)
                .flatMap(List::stream)
                .toList();
        cachingArtists(artistResults);
        artists.addAll(artistResults);

        return artists;
    }

    private List<ConfetiArtist> getArtistsByArtistIds(final List<String> artistIds) {
        Map<String, String> params = new HashMap<>();
        params.put("ids", String.join(QUERY_PARAMETER_IDS_DELIMITER, artistIds));

        return convertToConfetiArtists(
                restClient.request()
                        .get()
                        .baseUrl(appleMusicAPIURL.getBaseUrl())
                        .path(appleMusicAPIURL.getMultipleArtistsPath())
                        .params(MultiValueMap.fromSingleValue(params))
                        .build()
                        .connect(headers)
                        .retrieve(AppleMusicArtistsResponse.class)
        );
    }

    private List<ConfetiArtist> getCachedRelatedArtists(final String artistId, final int limit) {
        String key = String.format(REDIS_KEY_ARTISTS_RELATED, artistId, limit);
        Object raw = redisTemplate.opsForValue().get(key);
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
        String key = String.format(REDIS_KEY_ARTISTS_RELATED, artistId, limit);
        redisTemplate.opsForValue()
                .set(key, artists, REDIS_TTL_DAY, TimeUnit.DAYS);
    }

    @Override
    @RetryOnTokenExpire
    public List<ConfetiArtist> getRelatedArtists(final String artistId, final int limit) {
        List<ConfetiArtist> cachedArtists = new ArrayList<>(getCachedRelatedArtists(artistId, limit));
        if (!cachedArtists.isEmpty()) {
            return cachedArtists;
        }

        Map<String, String> params = new HashMap<>();
        params.put("limit", String.valueOf(limit));

        List<ConfetiArtist> artists = convertToConfetiArtists(
                restClient.request()
                        .get()
                        .baseUrl(appleMusicAPIURL.getBaseUrl())
                        .path(appleMusicAPIURL.getArtistRelationshipViewByNamePath(artistId,
                                ARTISTS_RELATIONSHIP_SIMILAR_VIEW))
                        .params(MultiValueMap.fromSingleValue(params))
                        .build()
                        .connect(headers)
                        .retrieve(AppleMusicArtistsResponse.class)
        );
        cacheRelatedArtists(artistId, limit, artists);

        return artists;
    }

    @Override
    @RetryOnTokenExpire
    public Optional<ConfetiArtist> findArtistByKeyword(final String keyword) {
        return findArtistsByKeyword(keyword, 1).stream()
                .findFirst();
    }

    @Override
    @RetryOnTokenExpire
    public List<ConfetiArtist> findArtistsByKeyword(final String keyword, final int limit) {
        Map<String, String> params = new HashMap<>();
        params.put("term", keyword);
        params.put("types", ARTISTS_TYPE);
        params.put("limit", String.valueOf(limit));
        params.put("with", "topResults");

        return convertToConfetiArtists(
                restClient.request()
                        .get()
                        .baseUrl(appleMusicAPIURL.getBaseUrl())
                        .path(appleMusicAPIURL.getSingleSearchPath())
                        .params(MultiValueMap.fromSingleValue(params))
                        .build()
                        .connect(headers)
                        .retrieve(AppleMusicSearchResponse.class)
        );
    }

    private Optional<ConfetiArtist> getCachedArtist(final String artistId) {
        Object raw = redisTemplate.opsForValue().get(REDIS_KEY_ARTISTS + artistId);

        if (Objects.isNull(raw)) {
            return Optional.empty();
        }

        return Optional.of(objectMapper.convertValue(raw, ConfetiArtist.class));
    }

    private void cachingArtist(final ConfetiArtist artist) {
        redisTemplate.opsForValue().set(REDIS_KEY_ARTISTS + artist.getId(), artist, REDIS_TTL_DAY, TimeUnit.DAYS);
    }

    @Override
    @RetryOnTokenExpire
    public Optional<ConfetiArtist> findArtistByArtistId(final String artistId) {
        Optional<ConfetiArtist> cachedArtist = getCachedArtist(artistId);
        if (cachedArtist.isPresent()) {
            return cachedArtist;
        }

        Optional<ConfetiArtist> artist = convertToConfetiArtist(
                restClient.request()
                        .get()
                        .baseUrl(appleMusicAPIURL.getBaseUrl())
                        .path(appleMusicAPIURL.getSingleArtistPath(artistId))
                        .build()
                        .connect(headers)
                        .retrieve(AppleMusicArtistsResponse.class)
        );

        artist.ifPresent(this::cachingArtist);

        return artist;
    }

    private List<ConfetiArtist> convertToConfetiArtists(final AppleMusicArtistsResponse artists) {
        if (Objects.isNull(artists)) {
            return List.of();
        }

        return artists.data().stream()
                .map(ConfetiArtist::from)
                .toList();
    }

    private List<ConfetiArtist> convertToConfetiArtists(final AppleMusicSearchResponse searchResult) {
        if (Objects.isNull(searchResult)) {
            return List.of();
        }

        return convertToConfetiArtists(searchResult.results().artists());
    }

    private Optional<ConfetiArtist> convertToConfetiArtist(final AppleMusicArtistsResponse artists) {
        if (Objects.isNull(artists)) {
            return Optional.of(ConfetiArtist.empty());
        }

        return Optional.of(
                artists.data().stream()
                        .findFirst()
                        .map(ConfetiArtist::from)
                        .orElse(ConfetiArtist.empty())
        );
    }

    private List<ConfetiAlbum> getCachedAlbums(final Set<String> albumIds) {
        Object raw = redisTemplate.opsForValue().multiGet(
                albumIds.stream()
                        .map(albumId -> REDIS_KEY_ALBUMS + albumId)
                        .collect(Collectors.toSet())
        );

        List<ConfetiAlbum> cachedAlbums = objectMapper.convertValue(raw, new TypeReference<List<ConfetiAlbum>>() {
                }).stream()
                .filter(Objects::nonNull)
                .toList();

        Set<String> cachedAlbumIds = cachedAlbums.stream()
                .map(ConfetiAlbum::getId)
                .collect(Collectors.toSet());
        ;

        albumIds.removeIf(cachedAlbumIds::contains);

        return cachedAlbums;
    }

    private void cachingAlbums(List<ConfetiAlbum> albums) {
        albums.forEach(album -> {
            redisTemplate.opsForValue().set(REDIS_KEY_ALBUMS + album.getId(), album, REDIS_TTL_DAY, TimeUnit.DAYS);
        });
    }

    @Override
    public List<ConfetiAlbum> getAlbumsByAlbumIds(final Set<String> albumIds) {
        if (albumIds.isEmpty()) {
            return Collections.emptyList();
        }

        List<ConfetiAlbum> albums = new ArrayList<>(getCachedAlbums(albumIds));

        AtomicInteger counter = new AtomicInteger();
        List<ConfetiAlbum> cacheMissedAlbums = albumIds.stream()
                .collect(Collectors.groupingBy(albumId -> counter.getAndIncrement() / ALBUMS_FETCH_LIMIT))
                .values().parallelStream()
                .map(this::getAlbumsByAlbumIds)
                .flatMap(List::stream)
                .toList();
        cachingAlbums(cacheMissedAlbums);
        albums.addAll(cacheMissedAlbums);

        return albums;
    }

    @RetryOnTokenExpire
    protected List<ConfetiAlbum> getAlbumsByAlbumIds(final List<String> albumIds) {
        Map<String, String> params = new HashMap<>();
        params.put("ids", String.join(QUERY_PARAMETER_IDS_DELIMITER, albumIds));

        return convertToConfetiAlbums(
                restClient.request()
                        .get()
                        .baseUrl(appleMusicAPIURL.getBaseUrl())
                        .path(appleMusicAPIURL.getMultipleAlbumsPath())
                        .params(MultiValueMap.fromSingleValue(params))
                        .build()
                        .connect(headers)
                        .retrieve(AppleMusicAlbumsResponse.class)
        );
    }

    private List<ConfetiAlbum> convertToConfetiAlbums(final AppleMusicAlbumsResponse albums) {
        return albums.data().stream()
                .map(ConfetiAlbum::from)
                .toList();
    }

    private List<ConfetiMusic> getCachedMusics(final Set<String> musicIds) {
        Object raw = redisTemplate.opsForValue().multiGet(
                musicIds.stream()
                        .map(musicId -> REDIS_KEY_MUSICS + musicId)
                        .collect(Collectors.toSet())
        );

        List<ConfetiMusic> cachedMusics = objectMapper.convertValue(raw, new TypeReference<List<ConfetiMusic>>() {
                }).stream()
                .filter(Objects::nonNull)
                .toList();

        Set<String> cachedMusicIds = cachedMusics.stream()
                .map(ConfetiMusic::getId)
                .collect(Collectors.toSet());
        ;

        musicIds.removeIf(cachedMusicIds::contains);

        return cachedMusics;
    }

    private void cachingMusics(final List<ConfetiMusic> musics) {
        musics.forEach(music -> {
            redisTemplate.opsForValue().set(REDIS_KEY_MUSICS + music.getId(), music, REDIS_TTL_DAY, TimeUnit.DAYS);
        });
    }

    @Override
    @RetryOnTokenExpire
    public List<ConfetiMusic> getMusicsByMusicIds(final Set<String> musicIds) {
        if (musicIds.isEmpty()) {
            return Collections.emptyList();
        }

        List<ConfetiMusic> musics = new ArrayList<>(getCachedMusics(musicIds));

        AtomicInteger counter = new AtomicInteger();
        List<ConfetiMusic> cacheMissedMusics = musicIds.stream()
                .collect(Collectors.groupingBy(musicId -> counter.getAndIncrement() / MUSICS_FETCH_LIMIT))
                .values().parallelStream()
                .map(this::getMusicsByMusicIds)
                .flatMap(List::stream)
                .toList();
        cachingMusics(cacheMissedMusics);
        musics.addAll(cacheMissedMusics);

        return musics;
    }

    private List<ConfetiMusic> getMusicsByMusicIds(final List<String> musicIds) {
        Map<String, String> params = new HashMap<>();
        params.put("ids", String.join(QUERY_PARAMETER_IDS_DELIMITER, musicIds));

        return convertToConfetiMusics(
                restClient.request()
                        .get()
                        .baseUrl(appleMusicAPIURL.getBaseUrl())
                        .path(appleMusicAPIURL.getMultipleSongsPath())
                        .params(MultiValueMap.fromSingleValue(params))
                        .build()
                        .connect(headers)
                        .retrieve(AppleMusicMusicsResponse.class)
        );
    }

    private List<ConfetiMusic> convertToConfetiMusics(final AppleMusicMusicsResponse musics) {
        return musics.data().stream()
                .map(ConfetiMusic::from)
                .toList();
    }

    private List<ConfetiMusic> getCachedTopMusics(final int fetchSize) {
        Object raw = redisTemplate.opsForValue().get(REDIS_KEY_TOP_MUSICS);

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
        redisTemplate.opsForValue().set(REDIS_KEY_TOP_MUSICS, musics, REDIS_TTL_DAY, TimeUnit.DAYS);
    }

    @Override
    @RetryOnTokenExpire
    public List<ConfetiMusic> getTopMusics(final int fetchSize) {
        validateFetchSize(fetchSize);

        List<ConfetiMusic> topMusics = new ArrayList<>(getCachedTopMusics(fetchSize));
        if (!topMusics.isEmpty()) {
            return topMusics;
        }

        Map<String, String> params = new HashMap<>();
        params.put("types", SONGS_TYPE);
        params.put("limit", String.valueOf(fetchSize));

        topMusics.addAll(convertToConfetiMusics(
                restClient.request()
                        .get()
                        .baseUrl(appleMusicAPIURL.getBaseUrl())
                        .path(appleMusicAPIURL.getChartsPath())
                        .params(MultiValueMap.fromSingleValue(params))
                        .build()
                        .connect(headers)
                        .retrieve(AppleMusicChartsResponse.class)
        ));
        cachingTopMusics(topMusics);

        return topMusics;
    }

    private List<ConfetiMusic> convertToConfetiMusics(final AppleMusicChartsResponse charts) {
        List<AppleMusicMusicResponse> musics = charts.results().songs().stream()
                .findFirst()
                .map(AppleMusicChartSongResponse::data)
                .orElseGet(List::of);

        return musics.stream()
                .map(ConfetiMusic::from)
                .toList();
    }

    private void validateFetchSize(int fetchSize) {
        if (fetchSize > CHARTS_FETCH_LIMIT) {
            throw new ConfetiException(ErrorMessage.BAD_REQUEST);
        }
    }

    private List<ConfetiMusic> getCachedTopMusicsByArtistId(final String artistId, final int fetchSize) {
        Object raw = redisTemplate.opsForValue().get(REDIS_KEY_ARTISTS_TOP_MUSICS + artistId);

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
        redisTemplate.opsForValue().set(REDIS_KEY_ARTISTS_TOP_MUSICS + artistId, musics, REDIS_TTL_DAY, TimeUnit.DAYS);
    }

    public List<ConfetiMusic> getTopSongsByArtistId(final String artistId, final int fetchSize) {
        List<ConfetiMusic> topMusics = new ArrayList<>(getCachedTopMusicsByArtistId(artistId, fetchSize));
        if (!topMusics.isEmpty()) {
            return topMusics;
        }

        Map<String, String> params = new HashMap<>();
        params.put("limit", String.valueOf(fetchSize));

        topMusics.addAll(convertToConfetiMusics(
                restClient.request()
                        .get()
                        .baseUrl(appleMusicAPIURL.getBaseUrl())
                        .path(appleMusicAPIURL.getArtistRelationshipViewByNamePath(artistId, "top-songs"))
                        .params(MultiValueMap.fromSingleValue(params))
                        .build()
                        .connect(headers)
                        .retrieve(AppleMusicMusicsResponse.class)
        ));
        cachingTopMusicsByArtistId(artistId, topMusics);

        return topMusics;
    }

    @Override
    @RetryOnTokenExpire
    public List<ConfetiMusic> getFilteredTopSongsByArtist(String artistId, int limit, Set<String> excludedMusicIds) {
        List<ConfetiMusic> songs = getTopSongsByArtistId(artistId, limit * 5);

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
        Object raw = redisTemplate.opsForValue()
                .get(String.format(REDIS_KEY_MUSIC_PAGE_ARTIST_OFFSET_LIMIT, artistId, offset, limit));

        if (Objects.isNull(raw)) {
            return Optional.empty();
        }

        return Optional.of(objectMapper.convertValue(raw, MusicPage.class));
    }

    private void cachingMusicPageByArtistId(String artistId, int offset, int limit, MusicPage musicPage) {
        redisTemplate.opsForValue()
                .set(String.format(REDIS_KEY_MUSIC_PAGE_ARTIST_OFFSET_LIMIT, artistId, offset, limit), musicPage,
                        REDIS_TTL_DAY, TimeUnit.DAYS);
    }

    @Override
    @RetryOnTokenExpire
    public MusicPage getArtistMusicsByArtistId(String artistId, int offset, int limit) {
        Optional<MusicPage> optMusicPage = getCachedMusicPageByArtistId(artistId, offset, limit);
        if (optMusicPage.isPresent()) {
            return optMusicPage.get();
        }

        Map<String, String> params = new HashMap<>();
        params.put("offset", String.valueOf(offset));
        params.put("limit", String.valueOf(limit));

        MusicPage musicPage = convertToConfetiMusicPage(
                restClient.request()
                        .get()
                        .baseUrl(appleMusicAPIURL.getBaseUrl())
                        .path(appleMusicAPIURL.getArtistSongsPath(artistId))
                        .params(MultiValueMap.fromSingleValue(params))
                        .build()
                        .connect(headers)
                        .retrieve(AppleMusicArtistMusicsResponse.class)
        );
        cachingMusicPageByArtistId(artistId, offset, limit, musicPage);

        return musicPage;
    }

    private MusicPage convertToConfetiMusicPage(AppleMusicArtistMusicsResponse artistMusics) {
        if (Objects.isNull(artistMusics.data())) {
            return MusicPage.empty();
        }

        return MusicPage.of(
                artistMusics.next(),
                artistMusics.data().stream()
                        .map(ConfetiMusic::from)
                        .toList()
        );
    }

    private Optional<MusicPage> getCachedMusicPageByKeyword(String term, int offset, int limit) {
        Object raw = redisTemplate.opsForValue()
                .get(String.format(REDIS_KEY_MUSIC_PAGE_KEYWORD_OFFSET_LIMIT, term, offset, limit));

        if (Objects.isNull(raw)) {
            return Optional.empty();
        }

        return Optional.of(objectMapper.convertValue(raw, MusicPage.class));
    }

    private void cachingMusicPageByKeyword(String term, int offset, int limit, MusicPage musicPage) {
        redisTemplate.opsForValue()
                .set(String.format(REDIS_KEY_MUSIC_PAGE_KEYWORD_OFFSET_LIMIT, term, offset, limit), musicPage,
                        REDIS_TTL_DAY, TimeUnit.DAYS);
    }

    @Override
    @RetryOnTokenExpire
    public MusicPage getMusicsByKeyword(String term, int offset, int limit) {
        Optional<MusicPage> optMusicPage = getCachedMusicPageByKeyword(term, offset, limit);
        if (optMusicPage.isPresent()) {
            return optMusicPage.get();
        }

        Map<String, String> params = new HashMap<>();
        params.put("term", term);
        params.put("offset", String.valueOf(offset));
        params.put("limit", String.valueOf(limit));
        params.put("types", SONGS_TYPE);

        MusicPage musicPage = convertToConfetiMusicPage(
                restClient.request()
                        .get()
                        .baseUrl(appleMusicAPIURL.getBaseUrl())
                        .path(appleMusicAPIURL.getSingleSearchPath())
                        .params(MultiValueMap.fromSingleValue(params))
                        .build()
                        .connect(headers)
                        .retrieve(AppleMusicSearchResponse.class)
        );
        cachingMusicPageByKeyword(term, offset, limit, musicPage);

        return musicPage;
    }

    private MusicPage convertToConfetiMusicPage(AppleMusicSearchResponse searchResult) {
        if (Objects.isNull(searchResult.results().songs())) {
            return MusicPage.empty();
        }

        AppleMusicMusicsResponse musics = searchResult.results().songs();

        return MusicPage.of(
                musics.next(),
                musics.data().stream()
                        .map(ConfetiMusic::from)
                        .toList()
        );
    }
}