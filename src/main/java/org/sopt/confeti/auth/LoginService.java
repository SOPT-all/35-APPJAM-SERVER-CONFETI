package org.sopt.confeti.auth;

import java.nio.file.Path;
import lombok.RequiredArgsConstructor;
import org.sopt.confeti.auth.command.LoginCommand;
import org.sopt.confeti.auth.dto.CreateUserDTO;
import org.sopt.confeti.auth.dto.LoginResult;
import org.sopt.confeti.auth.dto.OAuthSocialInfoResult;
import org.sopt.confeti.auth.jwt.JwtTokenGenerator;
import org.sopt.confeti.domain.token.AppleToken;
import org.sopt.confeti.domain.token.RefreshToken;
import org.sopt.confeti.domain.token.infra.AppleTokenRepository;
import org.sopt.confeti.domain.token.infra.RefreshTokenRepository;
import org.sopt.confeti.domain.user.AuthUser;
import org.sopt.confeti.domain.user.OAuthProvider;
import org.sopt.confeti.domain.user.constant.Role;
import org.sopt.confeti.global.common.constant.Default;
import org.sopt.confeti.global.common.constant.FolderPath;
import org.sopt.confeti.global.oauth.OAuthApiClient;
import org.sopt.confeti.global.oauth.OAuthApiClientRegistry;
import org.sopt.confeti.global.util.FileDownloader;
import org.sopt.confeti.global.util.S3FileHandler;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class LoginService {

    private final OAuthApiClientRegistry oAuthApiClientRegistry;
    private final JwtTokenGenerator jwtTokenGenerator;
    private final RefreshTokenRepository refreshTokenRepository;
    private final AppleTokenRepository appleTokenRepository;
    private final FileDownloader fileDownloader;
    private final S3FileHandler s3FileHandler;

    public OAuthSocialInfoResult getSocialInfo(LoginCommand command) {
        OAuthApiClient oAuthApiClient = oAuthApiClientRegistry.getOAuthApiClientByProvider(command.provider());
        return oAuthApiClient.getSocialInfo(command);
    }

    public CreateUserDTO getCreateUserDTO(OAuthProvider provider, OAuthSocialInfoResult socialInfo) {
        if (provider == OAuthProvider.KAKAO) {
            String profileImgUrl = downloadProfileImg(socialInfo.profileImgUrl());
            return CreateUserDTO.of(provider, socialInfo, profileImgUrl);
        }

        String defaultProfileImgName = getDefaultProfileImgName(socialInfo.name());
        return CreateUserDTO.of(provider, socialInfo, defaultProfileImgName);
    }

    private String downloadProfileImg(String profileImgUrl) {
        Path profileImg = fileDownloader.downloadFile(profileImgUrl);
        try {
            return s3FileHandler.uploadFile(profileImg.toFile(),
                    FolderPath.combine(FolderPath.USER, FolderPath.PROFILE));
        } finally {
            fileDownloader.deleteTempFile(profileImg);
        }
    }

    private String getDefaultProfileImgName(String socialName) {
        return s3FileHandler.copyFile(
                FolderPath.combine(FolderPath.USER, FolderPath.DEFAULT) + Default.PROFILE_IMG_NAME,
                FolderPath.combine(FolderPath.USER, FolderPath.PROFILE),
                socialName + Default.PROFILE_IMG_NAME
        );
    }

    @Transactional
    public Token createToken(AuthUser authUser, OAuthSocialInfoResult socialInfo) {
        Token token = new Token(
                jwtTokenGenerator.createAccessToken(String.valueOf(authUser.getId()), authUser.getRole(),
                        authUser.getProvider()),
                jwtTokenGenerator.createRefreshToken(String.valueOf(authUser.getId()), authUser.getRole(),
                        authUser.getProvider())
        );

        saveRefreshToken(token.refreshToken(), authUser.getId());
        saveSocialTokenIfAppleLogin(authUser.getProvider(), authUser.getId(), socialInfo);

        return token;
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

    private boolean isAppleLogin(OAuthProvider provider) {
        return provider.equals(OAuthProvider.APPLE);
    }

    public LoginResult getLoginResult(Token token, Role role) {
        return LoginResult.of(token, isOnboarding(role));
    }

    private boolean isOnboarding(Role role) {
        return role.equals(Role.ONBOARDING);
    }
}
