package org.sopt.confeti.domain.token.infra;

import org.sopt.confeti.domain.token.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
}
