package org.sopt.confeti.domain.user.infra.repository;

import lombok.RequiredArgsConstructor;
import org.sopt.confeti.domain.user.AuthUser;
import org.sopt.confeti.domain.user.User;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserRepositoryAdapter implements AllUserRepository {
    private final UserRepository userRepository;

    @Override
    public AuthUser save(AuthUser authUser){
        return userRepository.save(User.create(authUser)).toAuthUser();
    }
}
