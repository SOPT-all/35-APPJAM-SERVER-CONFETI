package org.sopt.confeti.auth;

import org.sopt.confeti.auth.dto.OAuthUserInfoResponse;
import org.sopt.confeti.auth.dto.OAuthLoginParams;
import org.sopt.confeti.auth.dto.OAuthTokenResponse;

public interface OAuthApiClient {
    OAuthTokenResponse requestAccessToken(OAuthLoginParams params);
    OAuthUserInfoResponse getOAuthUserInfo(String accessToken);
}