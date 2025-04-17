package org.sopt.confeti.api.user.controller;

import lombok.RequiredArgsConstructor;
import org.sopt.confeti.api.user.dto.response.onboard.UserOnboardRelatedArtistsResponse;
import org.sopt.confeti.api.user.facade.UserOnboardFacade;
import org.sopt.confeti.api.user.facade.dto.response.onboard.UserOnboardRelatedArtistsDTO;
import org.sopt.confeti.domain.user.constant.Role;
import org.sopt.confeti.global.annotation.Permission;
import org.sopt.confeti.global.common.BaseResponse;
import org.sopt.confeti.global.message.SuccessMessage;
import org.sopt.confeti.global.util.ApiResponseUtil;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user/onboard")
public class UserOnboardController {

    private final UserOnboardFacade userOnboardFacade;

    @Permission(role = {Role.ONBOARDING})
    @GetMapping("/artists/search")
    public ResponseEntity<BaseResponse<?>> getArtistsRelatedTerm(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String accessToken,
            @RequestParam String term,
            @RequestParam(defaultValue = "1") Integer limit
    ) {
        UserOnboardRelatedArtistsDTO relatedArtistsDTO = userOnboardFacade.getArtistsRelatedTerm(term, limit);
        return ApiResponseUtil.success(SuccessMessage.SUCCESS,
                UserOnboardRelatedArtistsResponse.from(relatedArtistsDTO));
    }
}
