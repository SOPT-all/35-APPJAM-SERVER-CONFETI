package org.sopt.confeti.api.user.facade;

import lombok.RequiredArgsConstructor;
import org.sopt.confeti.annotation.Facade;
import org.sopt.confeti.api.user.dto.response.UserInfoResponse;
import org.sopt.confeti.domain.user.application.UserService;
import org.springframework.transaction.annotation.Transactional;

@Facade
@RequiredArgsConstructor
public class UserInfoFacade {
    private final UserService userService;

    @Transactional
    public UserInfoResponse getUserInfo(Long userId) {
        UserInfoResponse urlInfo = userService.getUserInfo(userId);
        return urlInfo;
    }
}
