package org.sopt.confeti.global.oauth;

import org.sopt.confeti.auth.dto.OAuthUserInfoResult;
import org.sopt.confeti.auth.dto.OAuthLoginParams;
import org.sopt.confeti.auth.dto.OAuthTokenResult;
import org.sopt.confeti.domain.user.OAuthProvider;
import org.sopt.confeti.domain.user.constant.Role;

public interface OAuthApiClient {
    boolean supports(OAuthProvider provider);
    OAuthTokenResult requestAccessToken(OAuthLoginParams params);
    OAuthUserInfoResult getOAuthUserInfo(String accessToken);
}
