package org.sopt.confeti.api.dummy.facade;

import jakarta.annotation.PostConstruct;
import java.util.HashMap;
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

    @RetryOnTokenExpire
    public Object requestCatalogAlbum(String id, String views) {
        Map<String, String> params = new HashMap<>();
        if (views != null) {
            params.put("views", views);
        }

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
    public Object requestMultipleCatalogAlbums(String ids, String include) {
        Map<String, String> params = new HashMap<>();
        params.put("ids", ids);
        if (include != null) {
            params.put("include", include);
        }

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
    public Object requestCatalogArtist(String id, String views) {
        Map<String, String> params = new HashMap<>();
        if (views != null) {
            params.put("views", views);
        }

        return restClient.request()
                .get()
                .baseUrl(appleMusicAPIURL.getBaseUrl())
                .path(appleMusicAPIURL.getSingleArtistPath(id))
                .params(MultiValueMap.fromSingleValue(params))
                .build()
                .connect(headers)
                .retrieve(Object.class);
    }
}
