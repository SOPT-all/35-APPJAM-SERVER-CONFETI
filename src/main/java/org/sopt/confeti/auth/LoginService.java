package org.sopt.confeti.auth;

import lombok.RequiredArgsConstructor;
import org.sopt.confeti.auth.command.LoginCommand;
import org.sopt.confeti.auth.dto.LoginResult;
import org.sopt.confeti.auth.dto.OAuthLoginParams;
import org.sopt.confeti.auth.dto.OAuthTokenResult;
import org.sopt.confeti.auth.dto.OAuthUserInfoResult;
import org.sopt.confeti.auth.jwt.JwtTokenGenerator;
import org.sopt.confeti.domain.token.RefreshToken;
import org.sopt.confeti.domain.token.infra.RefreshTokenRepository;
import org.sopt.confeti.domain.user.AuthUser;
import org.sopt.confeti.domain.user.User;
import org.sopt.confeti.domain.user.infra.repository.AllUserRepository;
import org.sopt.confeti.domain.user.infra.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class LoginService {

    private final OAuthApiClient oAuthApiClient;
    private final UserRepository userRepository;
    private final AllUserRepository allUserRepository;
    private final JwtTokenGenerator jwtTokenGenerator;
    private final RefreshTokenRepository refreshTokenRepository;


    @Transactional
    public LoginResult login(LoginCommand command) {
        OAuthUserInfoResult socialUserInfo = getSocialInfo(command);
        AuthUser authUser = loadOrCreateUser(command, socialUserInfo);
        Token token = createToken(authUser);
        updateRefreshToken(token.refreshToken(), authUser.getId());
        return LoginResult.from(token);
    }

    private void updateRefreshToken(String refreshToken, long id) {
        refreshTokenRepository.save(
                RefreshToken.of(refreshToken, id)
        );
    }

    private AuthUser loadOrCreateUser(LoginCommand command, OAuthUserInfoResult socialUserInfo) {
        AuthUser retrievedAuthUser = userRepository.findBySocialIdAndProvider(
                        socialUserInfo.id(),
                        command.provider()
                )
                .map(User::toAuthUser)
                .orElse(null);

        if (retrievedAuthUser != null) {
            return retrievedAuthUser;
        }

        return allUserRepository.save(
                AuthUser.create(
                        command.provider(),
                        socialUserInfo.id(),
                        socialUserInfo.kakaoAccount().profile().nickname(),
                        socialUserInfo.kakaoAccount().profile().profileImageUrl()
                )
        );
    }

    private Token createToken(AuthUser authUser){
     return new Token(
             jwtTokenGenerator.createAccessToken(String.valueOf(authUser.getId())),
             jwtTokenGenerator.createRefreshToken(String.valueOf(authUser.getId()))
     );
    }

    private OAuthUserInfoResult getSocialInfo(LoginCommand command) {
        OAuthLoginParams loginParams = new OAuthLoginParams(command.redirectUrl(), command.code());
        OAuthTokenResult tokenResponse = oAuthApiClient.requestAccessToken(loginParams);
        OAuthUserInfoResult userInfo = oAuthApiClient.getOAuthUserInfo(tokenResponse.accessToken());
        return userInfo;
    }
}
