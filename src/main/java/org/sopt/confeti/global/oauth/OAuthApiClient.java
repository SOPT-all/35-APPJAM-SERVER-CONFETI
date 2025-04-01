package org.sopt.confeti.global.oauth;

import org.sopt.confeti.auth.command.LoginCommand;
import org.sopt.confeti.auth.dto.OAuthSocialInfoResult;
import org.sopt.confeti.domain.user.OAuthProvider;

public interface OAuthApiClient {

    boolean supports(OAuthProvider provider);

    OAuthSocialInfoResult getSocialInfo(LoginCommand command);
}
