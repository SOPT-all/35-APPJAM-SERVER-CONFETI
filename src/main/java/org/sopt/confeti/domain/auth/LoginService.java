package org.sopt.confeti.domain.auth;

import lombok.RequiredArgsConstructor;
import org.sopt.confeti.domain.auth.command.LoginCommand;
import org.sopt.confeti.domain.auth.dto.LoginResult;
import org.sopt.confeti.domain.auth.dto.KakaoLoginParams;
import org.sopt.confeti.domain.auth.dto.KakaoTokenResult;
import org.sopt.confeti.domain.auth.dto.KakaoSocialInfoResult;
import org.sopt.confeti.domain.auth.dto.OAuthSocialInfoResult;
import org.sopt.confeti.domain.auth.jwt.JwtTokenGenerator;
import org.sopt.confeti.domain.token.RefreshToken;
import org.sopt.confeti.domain.token.infra.RefreshTokenRepository;
import org.sopt.confeti.domain.user.AuthUser;
import org.sopt.confeti.domain.user.User;
import org.sopt.confeti.domain.user.infra.repository.AllUserRepository;
import org.sopt.confeti.domain.user.infra.repository.UserRepository;
import org.sopt.confeti.global.oauth.OAuthApiClient;
import org.sopt.confeti.global.oauth.OAuthApiClientRegistry;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class LoginService {

    private final OAuthApiClientRegistry oAuthApiClientRegistry;
    private final UserRepository userRepository;
    private final AllUserRepository allUserRepository;
    private final JwtTokenGenerator jwtTokenGenerator;
    private final RefreshTokenRepository refreshTokenRepository;


    @Transactional
    public LoginResult login(LoginCommand command) {
        OAuthApiClient oAuthApiClient = oAuthApiClientRegistry.getOAuthApiClientByProvider(command.provider());
        OAuthSocialInfoResult socialInfo = oAuthApiClient.getSocialInfo(command);
        AuthUser authUser = loadOrCreateUser(command, socialInfo);
        Token token = createToken(authUser);
        updateRefreshToken(token.refreshToken(), authUser.getId());
        return LoginResult.from(token);
    }

    private void updateRefreshToken(String refreshToken, long userId) {
        refreshTokenRepository.save(
                RefreshToken.of(refreshToken, userId)
        );
    }

    private AuthUser loadOrCreateUser(LoginCommand command, OAuthSocialInfoResult socialInfo) {
        AuthUser retrievedAuthUser = userRepository.findBySocialIdAndProvider(
                        socialInfo.id(),
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
                        socialInfo.id(),
                        socialInfo.name(),
                        socialInfo.profileImgUrl()
                )
        );
    }

    private Token createToken(AuthUser authUser){
     return new Token(
             jwtTokenGenerator.createAccessToken(String.valueOf(authUser.getId()), authUser.getRole()),
             jwtTokenGenerator.createRefreshToken(String.valueOf(authUser.getId()), authUser.getRole())
     );
    }
}
