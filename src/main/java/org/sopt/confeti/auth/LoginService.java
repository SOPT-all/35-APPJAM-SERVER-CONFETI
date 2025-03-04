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

          // 사용자 인증 후 사용자 정보 반환
          OAuthUserInfoResult socialUserInfo = getSocialInfo(command);
          // 사용자 입력값 + 사용자 정보로 실제 사용자인지 검사
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
        System.out.println("로그인 시도 - provider: " + command.provider() + ", socialId: " + socialUserInfo.id());

        // 사용자의 socialId로 사용자 가입돼 있는지 검사
        AuthUser retrievedAuthUser = userRepository.findBySocialIdAndProvider(
                        socialUserInfo.id(),
                        command.provider()
                )
                .map(User::toAuthUser)
                .orElse(null);

        if (retrievedAuthUser != null) {
            System.out.println("기존 사용자 발견 - userId: " + retrievedAuthUser.getId());
            return retrievedAuthUser;
        }

        System.out.println("기존 사용자 없음 - 새 사용자 생성 시작");

        // 사용자 없슨!! 새로 회원가입 해야대
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
