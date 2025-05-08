package org.sopt.confeti.api.auth.facade;

import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.sopt.confeti.api.auth.facade.dto.request.OnboardArtistDTO;
import org.sopt.confeti.api.auth.facade.dto.request.OnboardDTO;
import org.sopt.confeti.auth.LoginService;
import org.sopt.confeti.auth.LogoutService;
import org.sopt.confeti.auth.OnboardService;
import org.sopt.confeti.auth.ReissueService;
import org.sopt.confeti.auth.Token;
import org.sopt.confeti.auth.WithdrawService;
import org.sopt.confeti.auth.command.LoginCommand;
import org.sopt.confeti.auth.dto.LoginResult;
import org.sopt.confeti.auth.dto.OAuthSocialInfoResult;
import org.sopt.confeti.domain.artist_favorite.application.ArtistFavoriteService;
import org.sopt.confeti.domain.user.AuthUser;
import org.sopt.confeti.domain.user.OAuthProvider;
import org.sopt.confeti.domain.user.User;
import org.sopt.confeti.domain.user.application.UserService;
import org.sopt.confeti.domain.user.constant.Role;
import org.sopt.confeti.global.annotation.Facade;
import org.springframework.transaction.annotation.Transactional;

@Facade
@RequiredArgsConstructor
public class AuthFacade {

    private final LoginService loginService;
    private final ReissueService reissueService;
    private final LogoutService logoutService;
    private final UserService userService;
    private final ArtistFavoriteService artistFavoriteService;
    private final OnboardService onboardService;
    private final WithdrawService withdrawService;

    @Transactional
    public LoginResult login(LoginCommand loginCommand) {
        OAuthSocialInfoResult socialInfo = loginService.getSocialInfo(loginCommand);

        if (userService.notExist(socialInfo.id(), loginCommand.provider())) {
            userService.create(loginService.getCreateUserDTO(loginCommand.provider(), socialInfo));
        }

        AuthUser authUser = userService.getAuthUser(socialInfo.id(), loginCommand.provider());
        Token token = loginService.createToken(authUser, socialInfo);

        return loginService.getLoginResult(token, authUser.getRole());
    }

    @Transactional
    public Token reissue(String refreshToken) {
        return reissueService.reissue(refreshToken);
    }

    @Transactional
    public void logout(Long userId) {
        logoutService.logout(userId);
    }

    @Transactional
    public void onboard(long userId, OnboardDTO onboardDTO) {
        User user = userService.findById(userId);
        Set<String> favoriteArtistIds = onboardDTO.favoriteArtists().stream()
                .map(OnboardArtistDTO::artistId)
                .collect(Collectors.toSet());

        onboardService.validateFavoriteArtistCount(favoriteArtistIds);
        favoriteArtistIds.forEach(favoriteArtistId ->
                artistFavoriteService.addFavorite(user, favoriteArtistId)
        );
        user.setRole(Role.GENERAL);
    }

    @Transactional
    public void withdraw(long userId) {
        User user = userService.findById(userId);

        if (user.getProvider() == OAuthProvider.KAKAO) {
            withdrawService.unlinkKakaoAccount(user.getSocialId());
        }

        if (user.getProvider() == OAuthProvider.APPLE) {
            withdrawService.unlinkAppleAccount(user.getId());
        }

        userService.deleteUser(user);
        withdrawService.deleteAllExistAppleTokensByUserId(userId);
        withdrawService.deleteAllExistRefreshTokensByUserId(userId);
    }
}
