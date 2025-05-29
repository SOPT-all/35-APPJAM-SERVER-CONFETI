package org.sopt.confeti.api.user.facade;

import lombok.RequiredArgsConstructor;
import org.sopt.confeti.api.user.dto.request.PatchUserInfoRequest;
import org.sopt.confeti.api.user.facade.dto.response.UserInfoDTO;
import org.sopt.confeti.domain.user.application.UserService;
import org.sopt.confeti.global.annotation.Facade;
import org.sopt.confeti.global.exception.ConfetiException;
import org.sopt.confeti.global.exception.UnauthorizedException;
import org.sopt.confeti.global.message.ErrorMessage;
import org.springframework.transaction.annotation.Transactional;

@Facade
@RequiredArgsConstructor
public class UserInfoFacade {
    private final UserService userService;

    @Transactional
    public UserInfoDTO getUserInfo(Long userId) {
        return UserInfoDTO.from(
                userService.findById(userId)
        );
    }

    @Transactional
    public void patchUserInfo(Long userId, PatchUserInfoRequest patchUserInfoRequest) {
        validateExistUser(userId);
        validateUserInfoRequest(patchUserInfoRequest);

        userService.patchUserInfo(userId, patchUserInfoRequest);
    }

    protected void validateExistUser(final long userId) {
        if (!userService.existsById(userId)) {
            throw new UnauthorizedException(ErrorMessage.UNAUTHORIZED);
        }
    }

    protected void validateUserInfoRequest(PatchUserInfoRequest patchUserInfoRequest) {
        if (patchUserInfoRequest.name() == null && patchUserInfoRequest.profileUrl() == null) {
            throw new ConfetiException(ErrorMessage.BAD_REQUEST);
        }
    }
}
