package org.sopt.confeti.global.oauth;

import lombok.RequiredArgsConstructor;
import org.sopt.confeti.domain.auth.dto.KakaoLoginParams;
import org.sopt.confeti.domain.auth.dto.KakaoTokenResult;
import org.sopt.confeti.domain.auth.dto.KakaoSocialInfoResult;
import org.sopt.confeti.domain.user.OAuthProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
@RequiredArgsConstructor
public class AppleApiClient {

    @Value("${apple.client-id}")
    private String clientId;

    @Value("${apple.team-id}")
    private String teamId;

    @Value("${apple.key-id}")
    private String keyId;

    @Value("${apple.key-path}")
    private String keyPath;

    private static final String GRANT_TYPE = "authorization_code";
    private final String AAUTH_TOKEN_URL_HOST = "https://appleid.apple.com/auth/token";
    private final String AAUTH_PUBLIC_KEY_URL_HOST = "https://appleid.apple.com/auth/keys";

    private final RestClient restClient;

    public boolean supports(OAuthProvider provider) {
        return provider == OAuthProvider.APPLE;
    }

    public KakaoTokenResult requestAccessToken(KakaoLoginParams params) {
        return restClient
                .method(HttpMethod.POST)
                .uri(AAUTH_TOKEN_URL_HOST)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(createHttpBody(params))
                .retrieve()
                .toEntity(KakaoTokenResult.class)
                .getBody();
    }

    public KakaoSocialInfoResult getOAuthUserInfo(String accessToken) {
        return restClient
                .method(HttpMethod.GET)
                .uri(AAUTH_TOKEN_URL_HOST)
                .header("Authorization", createAuthorizationHeader(accessToken))
                .retrieve()
                .toEntity(KakaoSocialInfoResult.class)
                .getBody();
    }

    private String createHttpBody(KakaoLoginParams params) {
        return "grant_type=authorization_code" +
                "&client_id=" + clientId +
                "&redirect_uri=" + params.redirectUrl() +
                "&code=" + params.code();
    }

    private String createAuthorizationHeader(String accessToken) {
        return String.format("Bearer %s", accessToken);
    }
}
