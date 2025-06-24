package org.sopt.confeti.api.auth.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.sopt.confeti.api.auth.dto.request.LoginRequest;
import org.sopt.confeti.api.auth.dto.request.OnboardRequest;
import org.sopt.confeti.api.auth.facade.AuthFacade;
import org.sopt.confeti.api.auth.facade.dto.request.OnboardDTO;
import org.sopt.confeti.auth.Token;
import org.sopt.confeti.auth.command.LoginCommand;
import org.sopt.confeti.auth.dto.LoginResult;
import org.sopt.confeti.domain.user.constant.Role;
import org.sopt.confeti.global.annotation.Permission;
import org.sopt.confeti.global.annotation.UserId;
import org.sopt.confeti.global.common.BaseResponse;
import org.sopt.confeti.global.message.SuccessMessage;
import org.sopt.confeti.global.util.ApiResponseUtil;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final AuthFacade authFacade;

    @PostMapping("/login")
    public ResponseEntity<BaseResponse<?>> login(
            @Valid @RequestBody LoginRequest request
    ) {
        LoginResult result = authFacade.login(LoginCommand.from(request));
        return ApiResponseUtil.success(SuccessMessage.SUCCESS, result);
    }

    @Permission(role = {Role.ONBOARDING, Role.GENERAL})
    @PostMapping("/reissue")
    public ResponseEntity<BaseResponse<?>> reissue(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String refreshToken
    ) {
        Token token = authFacade.reissue(refreshToken);
        return ApiResponseUtil.success(SuccessMessage.SUCCESS, token);
    }

    @Permission(role = {Role.ONBOARDING, Role.GENERAL})
    @PostMapping("/logout")
    public ResponseEntity<BaseResponse<?>> logout(@UserId Long userId) {
        authFacade.logout(userId);
        return ApiResponseUtil.success(SuccessMessage.SUCCESS);
    }

    @Permission(role = {Role.ONBOARDING})
    @PostMapping("/onboard")
    public ResponseEntity<BaseResponse<?>> onboard(
            @UserId Long userId,
            @Valid @RequestBody OnboardRequest request
    ) {
        authFacade.onboard(userId, OnboardDTO.from(request));
        authFacade.flushCachedTopArtists(userId);
        return ApiResponseUtil.success(SuccessMessage.SUCCESS);
    }

    @Permission(role = {Role.GENERAL})
    @DeleteMapping("/withdraw")
    public ResponseEntity<BaseResponse<?>> withdraw(
            @UserId Long userId
    ) {
        authFacade.withdraw(userId);
        return ApiResponseUtil.success(SuccessMessage.SUCCESS);
    }
}
