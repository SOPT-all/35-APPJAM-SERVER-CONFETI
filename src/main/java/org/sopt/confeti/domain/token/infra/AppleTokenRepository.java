package org.sopt.confeti.domain.token.infra;

import org.sopt.confeti.domain.token.AppleToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AppleTokenRepository extends JpaRepository<AppleToken, Long> {
    void deleteAllByUserId(long userId);
}
