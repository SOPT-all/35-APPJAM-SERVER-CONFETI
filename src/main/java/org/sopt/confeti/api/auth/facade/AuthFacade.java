package org.sopt.confeti.api.auth.facade;

import lombok.RequiredArgsConstructor;
import org.sopt.confeti.auth.LoginService;
import org.sopt.confeti.auth.ReissueService;
import org.sopt.confeti.auth.Token;
import org.sopt.confeti.auth.command.LoginCommand;
import org.sopt.confeti.auth.dto.LoginResult;
import org.sopt.confeti.global.annotation.Facade;
import org.springframework.transaction.annotation.Transactional;

@Facade
@RequiredArgsConstructor
public class AuthFacade {
    private final LoginService loginService;
    private final ReissueService reissueService;

    @Transactional
    public LoginResult login(LoginCommand loginCommand) {
        return loginService.login(loginCommand);
    }

    @Transactional
    public Token reissue(String refreshToken) {
        return reissueService.reissue(refreshToken);
    }
}
