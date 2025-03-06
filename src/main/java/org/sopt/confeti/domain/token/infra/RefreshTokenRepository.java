package org.sopt.confeti.domain.token.infra;

import org.sopt.confeti.domain.token.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByRefreshToken(String refreshToken);
    void deleteAllByRefreshTokenAndUserId(String refreshToken, long userId);
    void deleteAllByUserId(long userId);
}
