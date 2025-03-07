package org.sopt.confeti.api.auth.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.sopt.confeti.api.auth.dto.LoginRequest;
import org.sopt.confeti.api.auth.facade.AuthFacade;
import org.sopt.confeti.domain.auth.Token;
import org.sopt.confeti.domain.auth.command.LoginCommand;
import org.sopt.confeti.domain.auth.dto.LoginResult;
import org.sopt.confeti.global.annotation.UserId;
import org.sopt.confeti.global.common.BaseResponse;
import org.sopt.confeti.global.message.SuccessMessage;
import org.sopt.confeti.global.util.ApiResponseUtil;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final AuthFacade authFacade;

    @PostMapping("/kakao/login")
    public ResponseEntity<BaseResponse<?>> login(
           @Valid @RequestBody LoginRequest request
    ) {
        LoginResult result = authFacade.login(new LoginCommand(request.provider(), request.redirectUrl(), request.code()));
        return ApiResponseUtil.success(SuccessMessage.SUCCESS, result);
    }

    @PostMapping("/kakao/reissue")
    public ResponseEntity<BaseResponse<?>> reissue(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String refreshToken
    ) {
        Token token = authFacade.reissue(refreshToken);
        return ApiResponseUtil.success(SuccessMessage.SUCCESS, token);
    }

    @PostMapping("/kakao/logout")
    public ResponseEntity<BaseResponse<?>> logout(@UserId Long userId){
        authFacade.logout(userId);
        return ApiResponseUtil.success(SuccessMessage.SUCCESS);
    }
}
