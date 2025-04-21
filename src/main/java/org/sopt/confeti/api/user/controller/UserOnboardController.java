package org.sopt.confeti.api.user.controller;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.sopt.confeti.api.user.dto.response.UserOnboardTopArtistsResponse;
import org.sopt.confeti.api.user.dto.response.onboard.UserOnboardRelatedArtistsResponse;
import org.sopt.confeti.api.user.facade.UserOnboardFacade;
import org.sopt.confeti.api.user.facade.dto.response.UserOnboardTopArtistsDTO;
import org.sopt.confeti.api.user.facade.dto.response.onboard.UserOnboardRelatedArtistsDTO;
import org.sopt.confeti.domain.user.constant.Role;
import org.sopt.confeti.global.annotation.Permission;
import org.sopt.confeti.global.annotation.UserId;
import org.sopt.confeti.global.common.BaseResponse;
import org.sopt.confeti.global.message.SuccessMessage;
import org.sopt.confeti.global.util.ApiResponseUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user/onboard")
@Validated
public class UserOnboardController {

    private final UserOnboardFacade userOnboardFacade;

    @Permission(role = {Role.ONBOARDING})

    @GetMapping("/artists/{artistId}/related")
    public ResponseEntity<BaseResponse<?>> getRelatedArtists(
            @UserId Long userId,
            @PathVariable String artistId,
            @RequestParam(defaultValue = "1") @Min(1) @Max(30) Integer limit
    ) {
        UserOnboardRelatedArtistsDTO relatedArtists = userOnboardFacade.getRelatedArtists(artistId, limit);
        return ApiResponseUtil.success(SuccessMessage.SUCCESS, UserOnboardRelatedArtistsResponse.from(relatedArtists));
    }

    @GetMapping("/artists/search")
    public ResponseEntity<BaseResponse<?>> getArtistsRelatedTerm(
            @UserId Long userId,
            @RequestParam String term,
            @RequestParam(defaultValue = "1") @Min(1) @Max(25) Integer limit
    ) {
        UserOnboardRelatedArtistsDTO relatedArtistsDTO = userOnboardFacade.getArtistsRelatedTerm(term, limit);
        return ApiResponseUtil.success(SuccessMessage.SUCCESS,
                UserOnboardRelatedArtistsResponse.from(relatedArtistsDTO));
    }

    @Permission(role = {Role.ONBOARDING})
    @GetMapping("/artists")
    public ResponseEntity<BaseResponse<?>> getTopArtists(
            @UserId Long userId
    ) {
        UserOnboardTopArtistsDTO topArtists = userOnboardFacade.getTopArtists();
        return ApiResponseUtil.success(SuccessMessage.SUCCESS, UserOnboardTopArtistsResponse.from(topArtists));
    }
}
