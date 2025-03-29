package org.sopt.confeti.global.oauth;

import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.sopt.confeti.auth.command.LoginCommand;
import org.sopt.confeti.auth.dto.OAuthSocialInfoResult;
import org.sopt.confeti.domain.user.OAuthProvider;
import org.sopt.confeti.global.annotation.OAuthClient;
import org.sopt.confeti.global.module.rest_client.builder.ApiRestClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.sopt.confeti.auth.dto.kakao.KakaoSocialInfoResult;
import org.sopt.confeti.auth.dto.kakao.KakaoLoginParams;
import org.sopt.confeti.auth.dto.kakao.KakaoTokenResult;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClient;

@OAuthClient
@RequiredArgsConstructor
public class KakaoApiClient implements OAuthApiClient {

    @Value("${kakao.client-id}")
    private String clientId;

    private static final String GRANT_TYPE = "authorization_code";
    private final String KAUTH_TOKEN_URL_HOST = "https://kauth.kakao.com/oauth/token";
    private final String KAUTH_USER_URL_HOST = "https://kapi.kakao.com/v2/user/me";

    private final ApiRestClientBuilder restClient;

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

    private KakaoTokenResult requestAccessToken(KakaoLoginParams requestParams) {
        Map<String, String> params = new HashMap<>();
        params.put("grant_type", GRANT_TYPE);
        params.put("client_id", clientId);
        params.put("redirect_uri", requestParams.redirectUrl());
        params.put("code", requestParams.code());

        return restClient.request()
                .post()
                .baseUrl(KAUTH_TOKEN_URL_HOST)
                .params(MultiValueMap.fromSingleValue(params))
                .build()
                .connect()
                .retrieve(KakaoTokenResult.class);
    }

    private KakaoSocialInfoResult getSocialInfo(String accessToken) {
        Map<String, String> headers = new HashMap<>();
        headers.put(HttpHeaders.AUTHORIZATION, createAuthorizationHeader(accessToken));

        return restClient.request()
                .post()
                .baseUrl(KAUTH_USER_URL_HOST)
                .build()
                .connect(headers)
                .retrieve(KakaoSocialInfoResult.class);
    }

    private String createAuthorizationHeader(String accessToken) {
        return String.format("Bearer %s", accessToken);
    }
}
