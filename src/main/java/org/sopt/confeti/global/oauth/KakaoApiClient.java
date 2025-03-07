package org.sopt.confeti.global.oauth;

import lombok.RequiredArgsConstructor;
import org.sopt.confeti.domain.auth.command.LoginCommand;
import org.sopt.confeti.domain.auth.dto.OAuthSocialInfoResult;
import org.sopt.confeti.domain.user.OAuthProvider;
import org.sopt.confeti.global.annotation.OAuthClient;
import org.springframework.beans.factory.annotation.Value;
import org.sopt.confeti.domain.auth.dto.kakao.KakaoSocialInfoResult;
import org.sopt.confeti.domain.auth.dto.kakao.KakaoLoginParams;
import org.sopt.confeti.domain.auth.dto.kakao.KakaoTokenResult;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestClient;

@OAuthClient
@RequiredArgsConstructor
public class KakaoApiClient implements OAuthApiClient {

    @Value("${kakao.client-id}")
    private String clientId;

    private static final String GRANT_TYPE = "authorization_code";
    private final String KAUTH_TOKEN_URL_HOST = "https://kauth.kakao.com/oauth/token";
    private final String KAUTH_USER_URL_HOST = "https://kapi.kakao.com/v2/user/me";

    private final RestClient restClient;

    @Override
    public boolean supports(OAuthProvider provider) {
        return provider == OAuthProvider.KAKAO;
    }

    @Override
    public OAuthSocialInfoResult getSocialInfo(LoginCommand command) {
        KakaoTokenResult tokenResult = requestAccessToken(KakaoLoginParams.from(command));
        KakaoSocialInfoResult socialInfo = getSocialInfo(tokenResult.accessToken());
        return OAuthSocialInfoResult.from(socialInfo);
    }

    private KakaoTokenResult requestAccessToken(KakaoLoginParams params) {
        return restClient
                .method(HttpMethod.POST)
                .uri(KAUTH_TOKEN_URL_HOST)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(createHttpBody(params))
                .retrieve()
                .toEntity(KakaoTokenResult.class)
                .getBody();
    }

    private KakaoSocialInfoResult getSocialInfo(String accessToken) {
        return restClient
                .method(HttpMethod.GET)
                .uri(KAUTH_USER_URL_HOST)
                .header("Authorization", createAuthorizationHeader(accessToken))
                .retrieve()
                .toEntity(KakaoSocialInfoResult.class)
                .getBody();
    }

    private String createHttpBody(KakaoLoginParams params) {
        return "grant_type=" + GRANT_TYPE +
                "&client_id=" + clientId +
                "&redirect_uri=" + params.redirectUrl() +
                "&code=" + params.code();
    }

    private String createAuthorizationHeader(String accessToken) {
        return String.format("Bearer %s", accessToken);
    }
}
