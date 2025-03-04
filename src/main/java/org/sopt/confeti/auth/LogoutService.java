package org.sopt.confeti.auth;

import lombok.RequiredArgsConstructor;
import org.sopt.confeti.domain.token.infra.RefreshTokenRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Transactional
public class LogoutService {
    private final RefreshTokenRepository refreshTokenRepository;

    @Transactional
    public void logout(long userId) {
        refreshTokenRepository.deleteAllByUserId(userId);
    }
}
