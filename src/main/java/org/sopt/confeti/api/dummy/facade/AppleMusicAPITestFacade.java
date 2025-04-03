package org.sopt.confeti.api.dummy.facade;

import jakarta.annotation.PostConstruct;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.sopt.confeti.global.annotation.Facade;
import org.sopt.confeti.global.annotation.RetryOnTokenExpire;
import org.sopt.confeti.global.module.rest_client.builder.ApiRestClientBuilder;
import org.sopt.confeti.global.util.music.AppleMusicAPITokenGenerator;
import org.sopt.confeti.global.util.music.AppleMusicAPIURL;
import org.springframework.http.HttpHeaders;
import org.springframework.util.MultiValueMap;

@Facade
@RequiredArgsConstructor
public class AppleMusicAPITestFacade {

    private final AppleMusicAPIURL appleMusicAPIURL;
    private final AppleMusicAPITokenGenerator tokenGenerator;
    private final ApiRestClientBuilder restClient;

    private String accessToken;
    private Map<String, String> headers = new HashMap<>();

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

    private void putIfNotEmpty(Map<String, String> params, String name, String target) {
        if (target != null && !target.isBlank()) {
            params.put(name, target);
        }
    }

    private void putIfNotEmpty(Map<String, String> params, String name, Integer target) {
        if (target != null) {
            params.put(name, target.toString());
        }
    }

    private void putIfNotEmpty(Map<String, String> params, String name, List<String> target) {
        if (target != null && !target.isEmpty()) {
            params.put(name, String.join(",", target));
        }
    }

    @RetryOnTokenExpire
    public Object requestCatalogAlbum(String id, String views, List<String> include) {
        Map<String, String> params = new HashMap<>();
        putIfNotEmpty(params, "views", views);
        putIfNotEmpty(params, "include", include);

        return restClient.request()
                .get()
                .baseUrl(appleMusicAPIURL.getBaseUrl())
                .path(appleMusicAPIURL.getSingleAlbumPath(id))
                .params(MultiValueMap.fromSingleValue(params))
                .build()
                .connect(headers)
                .retrieve(Object.class);
    }

    @RetryOnTokenExpire
    public Object requestMultipleCatalogAlbums(String ids, List<String> include) {
        Map<String, String> params = new HashMap<>();
        putIfNotEmpty(params, "ids", ids);
        putIfNotEmpty(params, "include", include);

        return restClient.request()
                .get()
                .baseUrl(appleMusicAPIURL.getBaseUrl())
                .path(appleMusicAPIURL.getMultipleAlbumsPath())
                .params(MultiValueMap.fromSingleValue(params))
                .build()
                .connect(headers)
                .retrieve(Object.class);
    }

    @RetryOnTokenExpire
    public Object requestCatalogAlbumRelationship(String id, String relationship, List<String> include, Integer limit) {
        Map<String, String> params = new HashMap<>();
        putIfNotEmpty(params, "include", include);
        putIfNotEmpty(params, "limit", limit);

        return restClient.request()
                .get()
                .baseUrl(appleMusicAPIURL.getBaseUrl())
                .path(appleMusicAPIURL.getAlbumRelationshipByNamePath(id, relationship))
                .params(MultiValueMap.fromSingleValue(params))
                .build()
                .connect(headers)
                .retrieve(Object.class);
    }

    @RetryOnTokenExpire
    public Object requestCatalogAlbumRelationshipView(String id, String view, List<String> include, Integer limit,
                                                      String with) {
        Map<String, String> params = new HashMap<>();
        putIfNotEmpty(params, "include", include);
        putIfNotEmpty(params, "limit", limit);
        putIfNotEmpty(params, "with", with);

        return restClient.request()
                .get()
                .baseUrl(appleMusicAPIURL.getBaseUrl())
                .path(appleMusicAPIURL.getAlbumRelationshipViewByNamePath(id, view))
                .params(MultiValueMap.fromSingleValue(params))
                .build()
                .connect(headers)
                .retrieve(Object.class);
    }

    @RetryOnTokenExpire
    public Object requestCatalogArtist(String id, String views, List<String> include) {
        Map<String, String> params = new HashMap<>();
        putIfNotEmpty(params, "views", views);
        putIfNotEmpty(params, "include", include);

        return restClient.request()
                .get()
                .baseUrl(appleMusicAPIURL.getBaseUrl())
                .path(appleMusicAPIURL.getSingleArtistPath(id))
                .params(MultiValueMap.fromSingleValue(params))
                .build()
                .connect(headers)
                .retrieve(Object.class);
    }

    @RetryOnTokenExpire
    public Object requestMultipleCatalogArtists(String ids, List<String> include) {
        Map<String, String> params = new HashMap<>();
        putIfNotEmpty(params, "ids", ids);
        putIfNotEmpty(params, "include", include);

        return restClient.request()
                .get()
                .baseUrl(appleMusicAPIURL.getBaseUrl())
                .path(appleMusicAPIURL.getMultipleArtistsPath())
                .params(MultiValueMap.fromSingleValue(params))
                .build()
                .connect(headers)
                .retrieve(Object.class);
    }

    @RetryOnTokenExpire
    public Object requestCatalogArtistRelationship(String id, String relationship, List<String> include,
                                                   Integer limit) {
        Map<String, String> params = new HashMap<>();
        putIfNotEmpty(params, "include", include);
        putIfNotEmpty(params, "limit", limit);

        return restClient.request()
                .get()
                .baseUrl(appleMusicAPIURL.getBaseUrl())
                .path(appleMusicAPIURL.getArtistRelationshipByNamePath(id, relationship))
                .params(MultiValueMap.fromSingleValue(params))
                .build()
                .connect(headers)
                .retrieve(Object.class);
    }

    @RetryOnTokenExpire
    public Object requestCatalogArtistRelationshipView(String id, String view, List<String> include, Integer limit,
                                                       String with) {
        Map<String, String> params = new HashMap<>();
        putIfNotEmpty(params, "include", include);
        putIfNotEmpty(params, "limit", limit);
        putIfNotEmpty(params, "with", with);

        return restClient.request()
                .get()
                .baseUrl(appleMusicAPIURL.getBaseUrl())
                .path(appleMusicAPIURL.getArtistRelationshipViewByNamePath(id, view))
                .params(MultiValueMap.fromSingleValue(params))
                .build()
                .connect(headers)
                .retrieve(Object.class);
    }

    @RetryOnTokenExpire
    public Object requestCatalogSong(String id, List<String> include) {
        Map<String, String> params = new HashMap<>();
        putIfNotEmpty(params, "include", include);

        return restClient.request()
                .get()
                .baseUrl(appleMusicAPIURL.getBaseUrl())
                .path(appleMusicAPIURL.getSingleSongPath(id))
                .params(MultiValueMap.fromSingleValue(params))
                .build()
                .connect(headers)
                .retrieve(Object.class);
    }

    @RetryOnTokenExpire
    public Object requestMultipleCatalogSongs(String ids, List<String> include) {
        Map<String, String> params = new HashMap<>();
        putIfNotEmpty(params, "ids", ids);
        putIfNotEmpty(params, "include", include);

        return restClient.request()
                .get()
                .baseUrl(appleMusicAPIURL.getBaseUrl())
                .path(appleMusicAPIURL.getMultipleSongsPath())
                .params(MultiValueMap.fromSingleValue(params))
                .build()
                .connect(headers)
                .retrieve(Object.class);
    }

    @RetryOnTokenExpire
    public Object requestSearch(String term, List<String> types, Integer limit, String offset) {
        Map<String, String> params = new HashMap<>();
        putIfNotEmpty(params, "term", term);
        putIfNotEmpty(params, "types", types);
        putIfNotEmpty(params, "limit", limit);
        putIfNotEmpty(params, "offset", offset);

        return restClient.request()
                .get()
                .baseUrl(appleMusicAPIURL.getBaseUrl())
                .path(appleMusicAPIURL.getSingleSearchPath())
                .params(MultiValueMap.fromSingleValue(params))
                .build()
                .connect(headers)
                .retrieve(Object.class);
    }

    @RetryOnTokenExpire
    public Object requestSearchHints(String term, Integer limit) {
        Map<String, String> params = new HashMap<>();
        putIfNotEmpty(params, "term", term);
        putIfNotEmpty(params, "limit", limit);

        return restClient.request()
                .get()
                .baseUrl(appleMusicAPIURL.getBaseUrl())
                .path(appleMusicAPIURL.getSearchHintsPath())
                .params(MultiValueMap.fromSingleValue(params))
                .build()
                .connect(headers)
                .retrieve(Object.class);
    }

    @RetryOnTokenExpire
    public Object requestSearchSuggestions(
            List<String> kinds,
            Integer limit,
            String term,
            List<String> types
    ) {
        Map<String, String> params = new HashMap<>();
        putIfNotEmpty(params, "kinds", kinds);
        putIfNotEmpty(params, "limit", limit);
        putIfNotEmpty(params, "term", term);
        putIfNotEmpty(params, "types", types);

        return restClient.request()
                .get()
                .baseUrl(appleMusicAPIURL.getBaseUrl())
                .path(appleMusicAPIURL.getSearchSuggestionsPath())
                .params(MultiValueMap.fromSingleValue(params))
                .build()
                .connect(headers)
                .retrieve(Object.class);
    }
}
