package org.sopt.confeti.auth;

import lombok.RequiredArgsConstructor;
import org.sopt.confeti.domain.token.infra.AppleTokenRepository;
import org.sopt.confeti.domain.token.infra.RefreshTokenRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class WithdrawService {
    private final AppleTokenRepository appleTokenRepository;
    private final RefreshTokenRepository refreshTokenRepository;

    @Transactional
    public void deleteAllExistAppleTokensByUserId(long userId) {
        appleTokenRepository.deleteAllByUserId(userId);
    }

    @Transactional
    public void deleteAllExistRefreshTokensByUserId(long userId) {
        refreshTokenRepository.deleteAllByUserId(userId);
    }
}
