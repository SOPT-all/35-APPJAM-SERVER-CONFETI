package org.sopt.confeti.api.user.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.sopt.confeti.api.user.controller.docs.UserOnboardControllerV4Docs;
import org.sopt.confeti.api.user.dto.request.PatchOnboardFavoriteArtistsRequest;
import org.sopt.confeti.api.user.dto.response.UserOnboardTopArtistsResponse;
import org.sopt.confeti.api.user.dto.response.onboard.GetOnboardStatusResponse;
import org.sopt.confeti.api.user.dto.response.onboard.UserOnboardFavoriteArtistsResponse;
import org.sopt.confeti.api.user.dto.response.onboard.UserOnboardRelatedArtistsResponse;
import org.sopt.confeti.api.user.facade.UserOnboardFacade;
import org.sopt.confeti.api.user.facade.dto.response.UserOnboardTopArtistsDTO;
import org.sopt.confeti.api.user.facade.dto.response.onboard.GetOnboardStatusDTO;
import org.sopt.confeti.api.user.facade.dto.response.onboard.UserOnboardFavoriteArtistsDTO;
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
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user/onboard/v4")
@Validated
public class UserOnboardControllerV4 implements UserOnboardControllerV4Docs {

    private final UserOnboardFacade userOnboardFacade;

    /**
     * 개발을 위해 임시로 Role.GENERAL 접근 허용
     */
    @Permission(role = {Role.ONBOARDING, Role.GENERAL})
    @GetMapping("/artists/{artistId}/related")
    public ResponseEntity<BaseResponse<UserOnboardRelatedArtistsResponse>> getRelatedArtists(
        @UserId Long userId,
        @PathVariable String artistId,
        @RequestParam(defaultValue = "1") @Min(1) @Max(30) Integer limit
    ) {
        UserOnboardRelatedArtistsDTO relatedArtists = userOnboardFacade.getRelatedArtists(userId,
            artistId, limit);
        return ApiResponseUtil.success(SuccessMessage.SUCCESS,
            UserOnboardRelatedArtistsResponse.from(relatedArtists));
    }

    @GetMapping("/artists/search")
    public ResponseEntity<BaseResponse<UserOnboardRelatedArtistsResponse>> getArtistsRelatedTerm(
        @UserId Long userId,
        @RequestParam String term,
        @RequestParam(defaultValue = "1") @Min(1) @Max(25) Integer limit
    ) {
        UserOnboardRelatedArtistsDTO relatedArtistsDTO = userOnboardFacade.getArtistsRelatedTerm(
            userId, term, limit);
        return ApiResponseUtil.success(SuccessMessage.SUCCESS,
            UserOnboardRelatedArtistsResponse.from(relatedArtistsDTO));
    }

    /**
     * 개발을 위해 임시로 Role.GENERAL 접근 허용
     */
    @Permission(role = {Role.ONBOARDING, Role.GENERAL})
    @GetMapping("/artists")
    public ResponseEntity<BaseResponse<UserOnboardTopArtistsResponse>> getTopArtists(
        @UserId Long userId,
        @RequestParam(required = false, defaultValue = "100") @Min(1) @Max(200) int limit
    ) {
        UserOnboardTopArtistsDTO topArtists = userOnboardFacade.getTopArtists(limit, userId);
        return ApiResponseUtil.success(SuccessMessage.SUCCESS,
            UserOnboardTopArtistsResponse.from(topArtists));
    }

    /**
     * 개발을 위해 임시로 Role.GENERAL 접근 허용
     */
    @Permission(role = {Role.ONBOARDING, Role.GENERAL})
    @PostMapping("/artists/{artistId}")
    public ResponseEntity<BaseResponse<Void>> addArtist(
        @UserId Long userId,
        @PathVariable String artistId
    ) {
        userOnboardFacade.cacheExposedArtist(userId, artistId);
        return ApiResponseUtil.success(SuccessMessage.SUCCESS);
    }

    @Permission(role = {Role.ONBOARDING, Role.GENERAL, Role.ADMIN})
    @GetMapping("/status")
    public ResponseEntity<BaseResponse<GetOnboardStatusResponse>> getOnboardStatus(
        @UserId Long userId) {
        GetOnboardStatusDTO onboardStatusDTO = userOnboardFacade.getOnboardStatus(userId);
        return ApiResponseUtil.success(SuccessMessage.SUCCESS,
            GetOnboardStatusResponse.from(onboardStatusDTO));
    }

    /**
     * 개발을 위해 임시로 Role.GENERAL 접근 허용
     */
    @Permission(role = {Role.ONBOARDING, Role.GENERAL})
    @GetMapping("/artists/favorite")
    public ResponseEntity<BaseResponse<UserOnboardFavoriteArtistsResponse>> getFavoriteArtists(
        @UserId Long userId
    ) {
        UserOnboardFavoriteArtistsDTO favoriteArtists = userOnboardFacade.getFavoriteArtists(
            userId);
        return ApiResponseUtil.success(SuccessMessage.SUCCESS,
            UserOnboardFavoriteArtistsResponse.from(favoriteArtists));
    }

    /**
     * 개발을 위해 임시로 Role.GENERAL 접근 허용
     */
    @Permission(role = {Role.ONBOARDING, Role.GENERAL})
    @PostMapping()
    public ResponseEntity<BaseResponse<Void>> onboard(
        @UserId Long userId
    ) {
        userOnboardFacade.onboard(userId);
        userOnboardFacade.flushCachedOnboardArtists(userId);
        return ApiResponseUtil.success(SuccessMessage.SUCCESS);
    }

    /**
     * 개발을 위해 임시로 Role.GENERAL 접근 허용
     */
    @Permission(role = {Role.ONBOARDING, Role.GENERAL})
    @PatchMapping("/artists/favorite")
    public ResponseEntity<BaseResponse<Void>> patchFavoriteArtists(
        @UserId Long userId,
        @Valid @RequestBody PatchOnboardFavoriteArtistsRequest request
    ) {
        userOnboardFacade.patchFavoriteArtist(userId, request.toDto());
        return ApiResponseUtil.success(SuccessMessage.SUCCESS);
    }

}
