package org.sopt.confeti.api.auth.facade;

import lombok.RequiredArgsConstructor;
import org.sopt.confeti.api.auth.facade.dto.request.OnboardDTO;
import org.sopt.confeti.auth.LoginService;
import org.sopt.confeti.auth.LogoutService;
import org.sopt.confeti.auth.OnboardService;
import org.sopt.confeti.auth.ReissueService;
import org.sopt.confeti.auth.Token;
import org.sopt.confeti.auth.WithdrawService;
import org.sopt.confeti.auth.command.LoginCommand;
import org.sopt.confeti.auth.dto.LoginResult;
import org.sopt.confeti.domain.artistfavorite.application.ArtistFavoriteService;
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
        return loginService.login(loginCommand);
    }

    @Transactional
    public Token reissue(String refreshToken) {
        return reissueService.reissue(refreshToken);
    }

    @Transactional
    public void logout(Long userId){
        logoutService.logout(userId);
    }

    @Transactional
    public void onboard(long userId, OnboardDTO onboardDTO) {
        User user = userService.findById(userId);
        onboardService.validateFavoriteArtistCount(onboardDTO);
        onboardDTO.favoriteArtists().forEach(favoriteArtist ->
                artistFavoriteService.addFavorite(user, favoriteArtist.artistId())
        );
        user.setRole(Role.GENERAL);
    }

    @Transactional
    public void withdraw(long userId) {
        User user = userService.findById(userId);
        userService.deleteUser(user);
        withdrawService.deleteAllExistTokensByUserId(userId);
    }
}
