package org.sopt.confeti.auth;

import lombok.RequiredArgsConstructor;
import org.sopt.confeti.domain.token.infra.AppleTokenRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class WithdrawService {

    private final AppleTokenRepository appleTokenRepository;

    @Transactional
    public void deleteAllExistTokensByUserId(long userId) {
        appleTokenRepository.deleteAllByUserId(userId);
    }
}
