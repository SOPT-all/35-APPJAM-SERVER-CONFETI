package org.sopt.confeti.api.user.facade;

import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.sopt.confeti.api.user.dto.request.PatchUserInfoRequest;
import org.sopt.confeti.domain.user.application.UserService;
import org.sopt.confeti.global.annotation.Facade;
import org.sopt.confeti.global.exception.ConfetiException;
import org.sopt.confeti.global.interceptor.auth.UserContext;
import org.sopt.confeti.global.message.ErrorMessage;

@Facade
@RequiredArgsConstructor
public class UserInfoFacade {

    private static final int MINIMUM_NAME_LENGTH = 2;
    private static final int MAXIMUM_NAME_LENGTH = 10;

    private final UserService userService;

    public void patchUserInfo(PatchUserInfoRequest patchUserInfoRequest) {
        validateUserInfoRequest(patchUserInfoRequest);

        userService.patchUserInfo(UserContext.get().id(), patchUserInfoRequest);
    }

    protected void validateUserInfoRequest(PatchUserInfoRequest patchUserInfoRequest) {
        if (Objects.nonNull(patchUserInfoRequest.profileFile())) {
            return;
        }

        if (Objects.isNull(patchUserInfoRequest.name())) {
            throw new ConfetiException(ErrorMessage.BAD_REQUEST);
        }

        String trimmedName = patchUserInfoRequest.name().trim();
        if (trimmedName.length() < MINIMUM_NAME_LENGTH
            || trimmedName.length() > MAXIMUM_NAME_LENGTH) {
            throw new ConfetiException(ErrorMessage.BAD_REQUEST);
        }
    }
}
