package org.sopt.confeti.domain.user.application;

import org.sopt.confeti.api.user.dto.response.UserInfoResponse;
import lombok.RequiredArgsConstructor;
import org.sopt.confeti.domain.user.User;
import org.sopt.confeti.domain.user.infra.repository.UserRepository;
import org.sopt.confeti.global.exception.NotFoundException;
import org.sopt.confeti.global.message.ErrorMessage;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public User findById(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(ErrorMessage.NOT_FOUND));
        return user;
    }

    public UserInfoResponse getUserInfo(Long userId) {
        User user = this.userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(ErrorMessage.NOT_FOUND));
        return new UserInfoResponse(
                user.getId(),
                user.getProfilePath(),
                user.getUsername()
        );
    }
}
