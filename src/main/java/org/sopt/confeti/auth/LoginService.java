package org.sopt.confeti.auth;

import lombok.RequiredArgsConstructor;
import org.sopt.confeti.auth.command.LoginCommand;
import org.sopt.confeti.auth.dto.LoginResult;
import org.sopt.confeti.auth.dto.OAuthSocialInfoResult;
import org.sopt.confeti.auth.jwt.JwtTokenGenerator;
import org.sopt.confeti.domain.token.AppleToken;
import org.sopt.confeti.domain.token.RefreshToken;
import org.sopt.confeti.domain.token.infra.AppleTokenRepository;
import org.sopt.confeti.domain.token.infra.RefreshTokenRepository;
import org.sopt.confeti.domain.user.AuthUser;
import org.sopt.confeti.domain.user.OAuthProvider;
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
    private final AppleTokenRepository appleTokenRepository;


    @Transactional
    public LoginResult login(LoginCommand command) {
        OAuthApiClient oAuthApiClient = oAuthApiClientRegistry.getOAuthApiClientByProvider(command.provider());
        OAuthSocialInfoResult socialInfo = oAuthApiClient.getSocialInfo(command);
        AuthUser authUser = loadOrCreateUser(command, socialInfo);
        Token token = createToken(authUser);
        saveRefreshToken(token.refreshToken(), authUser.getId());
        saveSocialTokenIfAppleLogin(command.provider(), authUser.getId(), socialInfo);

        return LoginResult.from(token);
    }

    private void saveRefreshToken(String refreshToken, long userId) {
        refreshTokenRepository.deleteAllByUserId(userId);
        refreshTokenRepository.save(
                RefreshToken.of(refreshToken, userId)
        );
    }

    private void saveSocialTokenIfAppleLogin(OAuthProvider provider, long userId, OAuthSocialInfoResult socialInfo) {
        if (isAppleLogin(provider)) {
            appleTokenRepository.deleteAllByUserId(userId);
            appleTokenRepository.save(
                    AppleToken.create(userId, socialInfo)
            );
        }
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
                 jwtTokenGenerator.createAccessToken(String.valueOf(authUser.getId()), authUser.getRole(), authUser.getProvider()),
                 jwtTokenGenerator.createRefreshToken(String.valueOf(authUser.getId()), authUser.getRole(), authUser.getProvider())
         );
    }

    private boolean isAppleLogin(OAuthProvider provider) {
        return provider.equals(OAuthProvider.APPLE);
    }
}
