package org.sopt.confeti.api.user.facade;

import lombok.RequiredArgsConstructor;
import org.sopt.confeti.api.user.dto.request.PatchUserInfoRequest;
import org.sopt.confeti.api.user.facade.dto.response.UserInfoDTO;
import org.sopt.confeti.domain.user.application.UserService;
import org.sopt.confeti.global.annotation.Facade;
import org.sopt.confeti.global.exception.ConfetiException;
import org.sopt.confeti.global.exception.UnauthorizedException;
import org.sopt.confeti.global.message.ErrorMessage;
import org.sopt.confeti.global.util.S3FileHandler;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Facade
@RequiredArgsConstructor
public class UserInfoFacade {

    private static final int MINIMUM_NAME_LENGTH = 2;
    private static final int MAXIMUM_NAME_LENGTH = 10;

    private final UserService userService;
    private final S3FileHandler s3FileHandler;

    @Transactional
    public UserInfoDTO getUserInfo(Long userId) {
        return UserInfoDTO.of(
                userService.findById(userId),
                s3FileHandler
        );
    }

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
        if (Objects.nonNull(patchUserInfoRequest.profileFile())) return;

        if (Objects.isNull(patchUserInfoRequest.name())) {
            throw new ConfetiException(ErrorMessage.BAD_REQUEST);
        }

        String trimmedName = patchUserInfoRequest.name().trim();
        if (trimmedName.length() < MINIMUM_NAME_LENGTH || trimmedName.length() > MAXIMUM_NAME_LENGTH) {
            throw new ConfetiException(ErrorMessage.BAD_REQUEST);
        }
    }
}
