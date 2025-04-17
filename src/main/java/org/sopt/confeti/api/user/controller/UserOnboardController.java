package org.sopt.confeti.api.user.controller;

import lombok.RequiredArgsConstructor;
import org.sopt.confeti.api.user.dto.response.UserOnboardTopArtistsResponse;
import org.sopt.confeti.api.user.facade.UserOnboardFacade;
import org.sopt.confeti.api.user.facade.dto.response.UserOnboardTopArtistsDTO;
import org.sopt.confeti.domain.user.constant.Role;
import org.sopt.confeti.global.annotation.Permission;
import org.sopt.confeti.global.annotation.UserId;
import org.sopt.confeti.global.common.BaseResponse;
import org.sopt.confeti.global.message.SuccessMessage;
import org.sopt.confeti.global.util.ApiResponseUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user/onboard")
public class UserOnboardController {

    private final UserOnboardFacade userOnboardFacade;

    @Permission(role = {Role.ONBOARDING})
    @GetMapping("/artists")
    public ResponseEntity<BaseResponse<?>> getTopArtists(
            @UserId Long userId
    ) {
        UserOnboardTopArtistsDTO topArtists = userOnboardFacade.getTopArtists();
        return ApiResponseUtil.success(SuccessMessage.SUCCESS, UserOnboardTopArtistsResponse.from(topArtists));
    }
}
