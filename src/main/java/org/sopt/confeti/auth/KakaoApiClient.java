package org.sopt.confeti.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.sopt.confeti.auth.dto.OAuthUserInfoResponse;
import org.sopt.confeti.auth.dto.OAuthLoginParams;
import org.sopt.confeti.auth.dto.OAuthTokenResponse;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
@RequiredArgsConstructor
public class KakaoApiClient implements OAuthApiClient {

    @Value("${kakao.client-id}")
    private String clientId;

    private static final String GRANT_TYPE = "authorization_code";
    private final String KAUTH_TOKEN_URL_HOST = "https://kauth.kakao.com/oauth/token";
    private final String KAUTH_USER_URL_HOST = "https://kapi.kakao.com/v2/user/me";

    private final RestClient restClient;

    @Override
    public OAuthTokenResponse requestAccessToken(OAuthLoginParams params) {
        return restClient
                .method(HttpMethod.POST)
                .uri(KAUTH_TOKEN_URL_HOST)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(createHttpBody(params))
                .retrieve()
                .toEntity(OAuthTokenResponse.class)
                .getBody();
    }

    @Override
    public OAuthUserInfoResponse getOAuthUserInfo(String accessToken) {
        return restClient
                .method(HttpMethod.GET)
                .uri(KAUTH_USER_URL_HOST)
                .header("Authorization", createAuthorizationHeader(accessToken))
                .retrieve()
                .toEntity(OAuthUserInfoResponse.class)
                .getBody();
    }

    private String createHttpBody(OAuthLoginParams params) {
        return "grant_type=authorization_code" +
                "&client_id=" + clientId +
                "&redirect_uri=" + params.redirectUrl() +
                "&code=" + params.code();
    }

    private String createAuthorizationHeader(String accessToken) {
        return String.format("Bearer %s", accessToken);
    }
}
