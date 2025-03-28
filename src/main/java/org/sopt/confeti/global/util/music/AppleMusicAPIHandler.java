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
import org.sopt.confeti.global.module.web_client.builder.ApiWebClientBuilder;
import org.sopt.confeti.global.resolver.artist.vo.ConfetiArtist;
import org.sopt.confeti.global.util.music.dto.artist.AppleMusicArtistsResponse;
import org.sopt.confeti.global.util.music.dto.search.AppleMusicSearchResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.util.MultiValueMap;
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
    private final ApiWebClientBuilder webClientBuilder;

    private final Map<String, String> headers = new ConcurrentHashMap<>();
    private String accessToken;

    @PostConstruct
    private void init() {
        accessToken = tokenGenerator.generateToken();
        headers.put(HttpHeaders.AUTHORIZATION, accessToken);
    }

    @Override
    public Mono<List<ConfetiArtist>> getArtistsByArtistIds(Set<String> artistIds) {
        if (artistIds.isEmpty()) {
            return Mono.just(Collections.emptyList());
        }

        AtomicInteger counter = new AtomicInteger();
        Map<Integer, List<String>> groupedIds =  artistIds.stream()
                        .collect(Collectors.groupingBy(artistId -> counter.getAndIncrement() / ARTISTS_FETCH_LIMIT));

        List<Mono<List<ConfetiArtist>>> monoArtistGroups = groupedIds.values().parallelStream()
                .map(this::getArtistsByArtistIds)
                .toList();

        return Flux.fromIterable(monoArtistGroups)
                .flatMap(mono -> mono)
                .collectList()
                .map(artists -> artists.stream()
                        .flatMap(List::stream)
                        .toList()
                );
    }

    private Mono<List<ConfetiArtist>> getArtistsByArtistIds(List<String> artistIds) {
        Map<String, String> params = new HashMap<>();
        params.put("ids", String.join(ARTISTS_MULTIPLE_DELIMITER, artistIds));

        // 애플 뮤직 api 사용
        return webClientBuilder.request()
                .get()
                .baseUrl(appleMusicAPIURL.getBaseUrl())
                .path(appleMusicAPIURL.getMultipleArtistsUrl())
                .params(MultiValueMap.fromSingleValue(params))
                .build()
                .connectSubscribe(headers, AppleMusicArtistsResponse.class)
                .map(this::convertToConfetiArtists);
    }

    @Override
    public Mono<Optional<ConfetiArtist>> findArtistByKeyword(String keyword) {
        Map<String, String> params = new HashMap<>();
        params.put("term", keyword);
        params.put("types", ARTISTS_TYPE);

        return webClientBuilder.request()
                .get()
                .baseUrl(appleMusicAPIURL.getBaseUrl())
                .path(appleMusicAPIURL.getSingleSearchUrl())
                .params(MultiValueMap.fromSingleValue(params))
                .build()
                .connectSubscribe(headers, AppleMusicSearchResponse.class)
                .map(this::convertToConfetiArtist);
    }

    @Override
    public Mono<Optional<ConfetiArtist>> findArtistByArtistId(String artistId) {
        return webClientBuilder.request()
                .get()
                .baseUrl(appleMusicAPIURL.getBaseUrl())
                .path(appleMusicAPIURL.getSingleArtistPath(artistId))
                .build()
                .connectSubscribe(headers, AppleMusicArtistsResponse.class)
                .map(this::convertToConfetiArtist);
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
}
