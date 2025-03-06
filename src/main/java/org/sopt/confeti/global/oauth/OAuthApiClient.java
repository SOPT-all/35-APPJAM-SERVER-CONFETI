package org.sopt.confeti.global.oauth;

import org.sopt.confeti.auth.dto.OAuthUserInfoResult;
import org.sopt.confeti.auth.dto.OAuthLoginParams;
import org.sopt.confeti.auth.dto.OAuthTokenResult;

public interface OAuthApiClient {
    OAuthTokenResult requestAccessToken(OAuthLoginParams params);
    OAuthUserInfoResult getOAuthUserInfo(String accessToken);
}