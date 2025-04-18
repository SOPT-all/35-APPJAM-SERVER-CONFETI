package org.sopt.confeti.global.util.music;

import jakarta.annotation.PostConstruct;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
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
import org.sopt.confeti.global.util.music.dto.music.AppleMusicMusicResponse;
import org.sopt.confeti.global.util.music.dto.music.AppleMusicMusicsResponse;
import org.sopt.confeti.global.util.music.dto.search.AppleMusicSearchResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.util.MultiValueMap;

@Handler
@RequiredArgsConstructor
public class AppleMusicAPIHandler implements MusicAPIHandler {

    private static final String QUERY_PARAMETER_IDS_DELIMITER = ",";
    private static final String ARTISTS_TYPE = "artists";
    private static final String SONGS_TYPE = "songs";

    // Fetch Limit 목록
    private static final int ARTISTS_FETCH_LIMIT = 25;
    private static final int ALBUMS_FETCH_LIMIT = 100;
    private static final int MUSICS_FETCH_LIMIT = 300;
    private static final int CHARTS_FETCH_LIMIT = 200;

    private final AppleMusicAPITokenGenerator tokenGenerator;
    private final AppleMusicAPIURL appleMusicAPIURL;
    private final ApiRestClientBuilder restClient;

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

    @Override
    @RetryOnTokenExpire
    public List<ConfetiArtist> getArtistsByArtistIds(final Set<String> artistIds) {
        if (artistIds.isEmpty()) {
            return Collections.emptyList();
        }

        AtomicInteger counter = new AtomicInteger();
        return artistIds.stream()
                .collect(Collectors.groupingBy(artistId -> counter.getAndIncrement() / ARTISTS_FETCH_LIMIT))
                .values().parallelStream()
                .map(this::getArtistsByArtistIds)
                .flatMap(List::stream)
                .toList();
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

    @Override
    @RetryOnTokenExpire
    public Optional<ConfetiArtist> findArtistByArtistId(final String artistId) {
        return convertToConfetiArtist(
                restClient.request()
                        .get()
                        .baseUrl(appleMusicAPIURL.getBaseUrl())
                        .path(appleMusicAPIURL.getSingleArtistPath(artistId))
                        .build()
                        .connect(headers)
                        .retrieve(AppleMusicArtistsResponse.class)
        );
    }

    private List<ConfetiArtist> convertToConfetiArtists(final AppleMusicArtistsResponse artists) {
        return artists.data().stream()
                .map(ConfetiArtist::from)
                .toList();
    }

    private List<ConfetiArtist> convertToConfetiArtists(final AppleMusicSearchResponse searchResult) {
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

    @Override
    public List<ConfetiAlbum> getAlbumsByAlbumIds(final Set<String> albumIds) {
        if (albumIds.isEmpty()) {
            return Collections.emptyList();
        }

        AtomicInteger counter = new AtomicInteger();
        return albumIds.stream()
                .collect(Collectors.groupingBy(albumId -> counter.getAndIncrement() / ALBUMS_FETCH_LIMIT))
                .values().parallelStream()
                .map(this::getAlbumsByAlbumIds)
                .flatMap(List::stream)
                .toList();
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

    @Override
    @RetryOnTokenExpire
    public List<ConfetiMusic> getMusicsByMusicIds(final Set<String> musicIds) {
        if (musicIds.isEmpty()) {
            return Collections.emptyList();
        }

        AtomicInteger counter = new AtomicInteger();
        return musicIds.stream()
                .collect(Collectors.groupingBy(musicId -> counter.getAndIncrement() / MUSICS_FETCH_LIMIT))
                .values().parallelStream()
                .map(this::getMusicsByMusicIds)
                .flatMap(List::stream)
                .toList();
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

    @Override
    @RetryOnTokenExpire
    public List<ConfetiMusic> getTopMusics(final int fetchSize) {
        validateFetchSize(fetchSize);

        Map<String, String> params = new HashMap<>();
        params.put("types", SONGS_TYPE);
        params.put("limit", String.valueOf(fetchSize));

        return convertToConfetiMusics(
                restClient.request()
                        .get()
                        .baseUrl(appleMusicAPIURL.getBaseUrl())
                        .path(appleMusicAPIURL.getChartsPath())
                        .params(MultiValueMap.fromSingleValue(params))
                        .build()
                        .connect(headers)
                        .retrieve(AppleMusicChartsResponse.class)
        );
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
}
