package org.sopt.confeti.domain.user.infra.repository;

import org.sopt.confeti.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
