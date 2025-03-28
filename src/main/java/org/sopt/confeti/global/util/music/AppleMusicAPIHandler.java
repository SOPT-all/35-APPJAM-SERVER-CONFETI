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
import org.sopt.confeti.global.module.rest_client.builder.ApiRestClientBuilder;
import org.sopt.confeti.global.module.web_client.builder.ApiWebClientBuilder;
import org.sopt.confeti.global.resolver.music_api.album.vo.ConfetiAlbum;
import org.sopt.confeti.global.resolver.music_api.artist.vo.ConfetiArtist;
import org.sopt.confeti.global.util.music.dto.artist.AppleMusicArtistsResponse;
import org.sopt.confeti.global.util.music.dto.search.AppleMusicSearchResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Handler
@RequiredArgsConstructor
public class AppleMusicAPIHandler implements MusicAPIHandler {

    private static final String ARTISTS_MULTIPLE_DELIMITER = ",";
    private static final String ARTISTS_TYPE = "artists";

    // Fetch Limit 목록
    private static final int ARTISTS_FETCH_LIMIT = 25;

    private final AppleMusicAPITokenGenerator tokenGenerator;
    private final AppleMusicAPIURL appleMusicAPIURL;
    private final ApiRestClientBuilder restClient;

    private final Map<String, String> headers = new ConcurrentHashMap<>();
    private String accessToken;

    @PostConstruct
    private void init() {
        accessToken = tokenGenerator.generateToken();
        headers.put(HttpHeaders.AUTHORIZATION, accessToken);
    }

    @Override
    public List<ConfetiArtist> getArtistsByArtistIds(Set<String> artistIds) {
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

    private List<ConfetiArtist> getArtistsByArtistIds(List<String> artistIds) {
        Map<String, String> params = new HashMap<>();
        params.put("ids", String.join(ARTISTS_MULTIPLE_DELIMITER, artistIds));

        return convertToConfetiArtists(
                restClient.request()
                .get()
                .baseUrl(appleMusicAPIURL.getBaseUrl())
                .path(appleMusicAPIURL.getMultipleArtistsUrl())
                .params(MultiValueMap.fromSingleValue(params))
                .build()
                .connect(headers)
                .retrieve(AppleMusicArtistsResponse.class)
        );
    }

    @Override
    public Optional<ConfetiArtist> findArtistByKeyword(String keyword) {
        Map<String, String> params = new HashMap<>();
        params.put("term", keyword);
        params.put("types", ARTISTS_TYPE);

        return convertToConfetiArtist(
                restClient.request()
                .get()
                .baseUrl(appleMusicAPIURL.getBaseUrl())
                .path(appleMusicAPIURL.getSingleSearchUrl())
                .params(MultiValueMap.fromSingleValue(params))
                .build()
                .connect(headers)
                .retrieve(AppleMusicSearchResponse.class)
        );
    }

    @Override
    public Optional<ConfetiArtist> findArtistByArtistId(String artistId) {
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

    private List<ConfetiArtist> convertToConfetiArtists(AppleMusicArtistsResponse artists) {
        return artists.data().stream()
                .map(ConfetiArtist::from)
                .toList();
    }

    private Optional<ConfetiArtist> convertToConfetiArtist(AppleMusicSearchResponse searchResult) {
        return convertToConfetiArtist(searchResult.results().artists());
    }

    private Optional<ConfetiArtist> convertToConfetiArtist(AppleMusicArtistsResponse artists) {
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
    public List<ConfetiAlbum> getAlbumsByAlbumIds(Set<String> albumIds) {
        return List.of();
    }
}
