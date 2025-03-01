package org.sopt.confeti.domain.user.infra.repository;

import org.sopt.confeti.domain.user.AuthUser;

public interface AllUserRepository {
    AuthUser save(AuthUser authUser);
}
