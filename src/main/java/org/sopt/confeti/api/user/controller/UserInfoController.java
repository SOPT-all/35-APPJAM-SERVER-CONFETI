package org.sopt.confeti.api.user.controller;

import lombok.RequiredArgsConstructor;
import org.sopt.confeti.api.user.dto.request.PatchUserInfoRequest;
import org.sopt.confeti.api.user.dto.response.UserInfoResponse;
import org.sopt.confeti.api.user.facade.UserInfoFacade;
import org.sopt.confeti.domain.user.constant.Role;
import org.sopt.confeti.global.annotation.Permission;
import org.sopt.confeti.global.common.BaseResponse;
import org.sopt.confeti.global.interceptor.auth.UserContext;
import org.sopt.confeti.global.message.SuccessMessage;
import org.sopt.confeti.global.util.ApiResponseUtil;
import org.sopt.confeti.global.util.S3FileHandler;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user/info")
public class UserInfoController {

    private final UserInfoFacade userInfoFacade;
    private final S3FileHandler s3FileHandler;

    @Permission(role = {Role.GENERAL})
    @GetMapping
    public ResponseEntity<BaseResponse<UserInfoResponse>> getUserInfo() {
        return ApiResponseUtil.success(SuccessMessage.SUCCESS,
            UserInfoResponse.of(UserContext.get(), s3FileHandler));
    }

    @Permission(role = {Role.GENERAL})
    @PatchMapping
    public ResponseEntity<BaseResponse<Void>> patchUserInfo(
        @ModelAttribute PatchUserInfoRequest patchUserInfoRequest
    ) {
        userInfoFacade.patchUserInfo(patchUserInfoRequest);
        return ApiResponseUtil.success(SuccessMessage.SUCCESS);
    }
}
