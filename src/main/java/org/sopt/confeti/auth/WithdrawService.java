package org.sopt.confeti.auth;

import lombok.RequiredArgsConstructor;
import org.sopt.confeti.domain.token.AppleToken;
import org.sopt.confeti.domain.token.infra.AppleTokenRepository;
import org.sopt.confeti.domain.token.infra.RefreshTokenRepository;
import org.sopt.confeti.global.exception.NotFoundException;
import org.sopt.confeti.global.message.ErrorMessage;
import org.sopt.confeti.global.oauth.AppleApiClient;
import org.sopt.confeti.global.oauth.KakaoApiClient;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class WithdrawService {

    private final AppleTokenRepository appleTokenRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final KakaoApiClient kakaoApiClient;
    private final AppleApiClient appleApiClient;

    @Transactional
    public void deleteAllExistAppleTokensByUserId(long userId) {
        appleTokenRepository.deleteAllByUserId(userId);
    }

    @Transactional
    public void deleteAllExistRefreshTokensByUserId(long userId) {
        refreshTokenRepository.deleteAllByUserId(userId);
    }

    public void unlinkKakaoAccount(String accessToken) {
        kakaoApiClient.unlink(accessToken);
    }

    public void unlinkAppleAccount(long userId) {
        AppleToken token = appleTokenRepository.findByUserId(userId)
                .orElseThrow(
                        () -> new NotFoundException(ErrorMessage.NOT_FOUND)
                );

        appleApiClient.unlink(token.getAccessToken());
    }
}
