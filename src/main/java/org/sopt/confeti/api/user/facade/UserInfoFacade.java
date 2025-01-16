package org.sopt.confeti.api.user.facade;

import lombok.RequiredArgsConstructor;
import org.sopt.confeti.annotation.Facade;
import org.sopt.confeti.api.user.facade.dto.response.UserInfoDTO;
import org.sopt.confeti.domain.user.User;
import org.sopt.confeti.domain.user.application.UserService;
import org.springframework.transaction.annotation.Transactional;

@Facade
@RequiredArgsConstructor
public class UserInfoFacade {
    private final UserService userService;

    @Transactional
        public UserInfoDTO getUserInfo(Long userId) {
        User user = userService.getUserInfo(userId);
        return new UserInfoDTO(
                user.getId(),
                user.getProfilePath(),
                user.getUsername()
        );
    }
}
