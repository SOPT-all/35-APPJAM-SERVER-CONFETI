package org.sopt.confeti.api.auth.facade;

import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.sopt.confeti.api.auth.facade.dto.request.OnboardArtistDTO;
import org.sopt.confeti.api.auth.facade.dto.request.OnboardDTO;
import org.sopt.confeti.auth.LoginService;
import org.sopt.confeti.auth.LogoutService;
import org.sopt.confeti.auth.OnboardService;
import org.sopt.confeti.auth.ReissueService;
import org.sopt.confeti.auth.Token;
import org.sopt.confeti.auth.WebhookService;
import org.sopt.confeti.auth.WithdrawService;
import org.sopt.confeti.auth.command.LoginCommand;
import org.sopt.confeti.auth.dto.LoginResult;
import org.sopt.confeti.auth.dto.OAuthSocialInfoResult;
import org.sopt.confeti.domain.admin_social_id.application.AdminSocialIdService;
import org.sopt.confeti.domain.artist_favorite.application.ArtistFavoriteService;
import org.sopt.confeti.domain.user.AuthUser;
import org.sopt.confeti.domain.user.OAuthProvider;
import org.sopt.confeti.domain.user.User;
import org.sopt.confeti.domain.user.application.UserOnboardService;
import org.sopt.confeti.domain.user.application.UserService;
import org.sopt.confeti.domain.user.constant.Role;
import org.sopt.confeti.global.annotation.Facade;
import org.sopt.confeti.global.interceptor.auth.UserContext;
import org.sopt.confeti.global.interceptor.auth.UserInfo;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Facade
@RequiredArgsConstructor
public class AuthFacade {

    private final LoginService loginService;
    private final ReissueService reissueService;
    private final LogoutService logoutService;
    private final UserService userService;
    private final AdminSocialIdService adminSocialIdService;
    private final ArtistFavoriteService artistFavoriteService;
    private final OnboardService onboardService;
    private final WithdrawService withdrawService;
    private final WebhookService webhookService;
    private final UserOnboardService userOnboardService;

    @Transactional
    public LoginResult login(LoginCommand loginCommand) {
        OAuthSocialInfoResult socialInfo = loginService.getSocialInfo(loginCommand);

        if (userService.notExist(socialInfo.id(), loginCommand.provider())) {
            userService.create(loginService.getCreateUserDTO(loginCommand.provider(), socialInfo));
            log.debug("Social ID : {}, User Name : {}", socialInfo.id(), socialInfo.name());
            webhookService.sendDiscordNotification();

            if (adminSocialIdService.isAdminSocialId(socialInfo.id())) {
                User newUser = userService.getBySocialIdAndProvider(socialInfo.id(), loginCommand.provider());
                newUser.setRole(Role.ADMIN);
            }
        }

        AuthUser authUser = userService.getAuthUser(socialInfo.id(), loginCommand.provider());
        Token token = loginService.createToken(authUser, socialInfo);
        reissueService.cacheRefreshToken(authUser.getId(), token.refreshToken());

        return loginService.getLoginResult(token, authUser.getRole());
    }

    @Transactional
    public Token reissue(String refreshToken) {
        return reissueService.reissue(refreshToken);
    }

    @Transactional
    public void logout() {
        logoutService.logout();
    }

    @Deprecated
    @Transactional
    public void onboard(long userId, OnboardDTO onboardDTO) {
        User user = userService.findById(userId);
        Set<String> favoriteArtistIds = onboardDTO.favoriteArtists().stream()
            .map(OnboardArtistDTO::artistId)
            .collect(Collectors.toSet());

        onboardService.validateFavoriteArtistCount(favoriteArtistIds);
        artistFavoriteService.addFavorites(user, favoriteArtistIds);
        if (user.getRole() != Role.ADMIN) {
            user.setRole(Role.GENERAL);
        }
    }

    public void flushCachedTopArtists(long userId) {
        userOnboardService.flushCachedOnboardArtists(userId);
    }

    @Transactional
    public void withdraw() {
        UserInfo userInfo = UserContext.get();
        User user = userService.findById(userInfo.id());

        if (user.getProvider() == OAuthProvider.KAKAO) {
            withdrawService.unlinkKakaoAccount(user.getSocialId());
        }

        if (user.getProvider() == OAuthProvider.APPLE) {
            withdrawService.unlinkAppleAccount(user.getId());
        }

        withdrawService.deleteAllExistAppleTokensByUserId(userInfo.id());
        withdrawService.deleteAllExistRefreshTokensByUserId(userInfo.id());
        userService.deleteUser(user);
    }
}
