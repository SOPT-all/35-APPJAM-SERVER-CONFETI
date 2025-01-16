package org.sopt.confeti.api.user.controller;

import lombok.RequiredArgsConstructor;
import org.sopt.confeti.api.user.facade.UserInfoFacade;
import org.sopt.confeti.global.common.BaseResponse;
import org.sopt.confeti.global.message.SuccessMessage;
import org.sopt.confeti.global.util.ApiResponseUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user/info")
public class UserInfoController {

    private final UserInfoFacade userInfoFacade;

    @GetMapping
    public ResponseEntity<BaseResponse<?>> getUserInfo(@RequestHeader("Authorization") Long userId) {
        return ApiResponseUtil.success(SuccessMessage.SUCCESS, userInfoFacade.getUserInfo(userId));
    }

}
