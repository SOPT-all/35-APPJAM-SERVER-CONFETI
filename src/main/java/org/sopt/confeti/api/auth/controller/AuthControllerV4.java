package org.sopt.confeti.api.auth.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.sopt.confeti.api.auth.dto.request.LoginRequest;
import org.sopt.confeti.api.auth.facade.AuthFacade;
import org.sopt.confeti.auth.Token;
import org.sopt.confeti.auth.command.LoginCommand;
import org.sopt.confeti.auth.dto.LoginResult;
import org.sopt.confeti.domain.user.constant.Role;
import org.sopt.confeti.global.annotation.Permission;
import org.sopt.confeti.global.annotation.RefreshToken;
import org.sopt.confeti.global.annotation.UserId;
import org.sopt.confeti.global.common.BaseResponse;
import org.sopt.confeti.global.message.SuccessMessage;
import org.sopt.confeti.global.util.ApiResponseUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequiredArgsConstructor
@RequestMapping("/auth/v4")
public class AuthControllerV4 {

    private final AuthFacade authFacade;

    @PostMapping("/login")
    public ResponseEntity<BaseResponse<LoginResult>> login(
        @Valid @RequestBody LoginRequest request
    ) {
        LoginResult result = authFacade.login(LoginCommand.from(request));
        return ApiResponseUtil.success(SuccessMessage.SUCCESS, result);
    }

    @Permission(role = {Role.ONBOARDING, Role.GENERAL})
    @PostMapping("/reissue")
    public ResponseEntity<BaseResponse<Token>> reissue(
        @RefreshToken String refreshToken
    ) {
        Token token = authFacade.reissue(refreshToken);
        return ApiResponseUtil.success(SuccessMessage.SUCCESS, token);
    }

    @Permission(role = {Role.ONBOARDING, Role.GENERAL})
    @PostMapping("/logout")
    public ResponseEntity<BaseResponse<Void>> logout(@UserId Long userId) {
        authFacade.logout(userId);
        return ApiResponseUtil.success(SuccessMessage.SUCCESS);
    }

    /**
     * 개발을 위해 임시로 Role.GENERAL 접근 허용
     */
    @Permission(role = {Role.ONBOARDING, Role.GENERAL})
    @PostMapping("/onboard")
    public ResponseEntity<BaseResponse<Void>> onboard(
        @UserId Long userId
    ) {
        authFacade.onboard(userId);
        authFacade.flushCachedTopArtists(userId);
        return ApiResponseUtil.success(SuccessMessage.SUCCESS);
    }

    @Permission(role = {Role.GENERAL})
    @DeleteMapping("/withdraw")
    public ResponseEntity<BaseResponse<Void>> withdraw(
        @UserId Long userId
    ) {
        authFacade.withdraw(userId);
        return ApiResponseUtil.success(SuccessMessage.SUCCESS);
    }

}
