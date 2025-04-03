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
                .path(appleMusicAPIURL.getSingleAlbumUrl(id))
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
                .path(appleMusicAPIURL.getMultipleAlbumsUrl())
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
                .path(appleMusicAPIURL.getMultipleArtistsUrl())
                .params(MultiValueMap.fromSingleValue(params))
                .build()
                .connect(headers)
                .retrieve(Object.class);
    }
}
