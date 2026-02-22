package org.sopt.confeti.api.user.controller;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.sopt.confeti.api.user.controller.docs.UserOnboardControllerDocs;
import org.sopt.confeti.api.user.dto.request.AddOnboardFavoriteArtistRequest;
import org.sopt.confeti.api.user.dto.request.PatchOnboardFavoriteArtistsRequest;
import org.sopt.confeti.api.user.dto.response.UserOnboardTopArtistsResponse;
import org.sopt.confeti.api.user.dto.response.onboard.GetOnboardStatusResponse;
import org.sopt.confeti.api.user.dto.response.onboard.UserOnboardArtistsResponse;
import org.sopt.confeti.api.user.dto.response.onboard.UserOnboardFavoriteArtistsResponse;
import org.sopt.confeti.api.user.dto.response.onboard.UserOnboardRelatedArtistsResponse;
import org.sopt.confeti.api.user.facade.UserOnboardFacade;
import org.sopt.confeti.api.user.facade.dto.response.UserOnboardTopArtistsDTO;
import org.sopt.confeti.api.user.facade.dto.response.onboard.GetOnboardStatusDTO;
import org.sopt.confeti.api.user.facade.dto.response.onboard.UserOnboardArtistsDTO;
import org.sopt.confeti.api.user.facade.dto.response.onboard.UserOnboardFavoriteArtistsDTO;
import org.sopt.confeti.api.user.facade.dto.response.onboard.UserOnboardRelatedArtistsDTO;
import org.sopt.confeti.global.annotation.ApiVersion;
import org.sopt.confeti.global.annotation.Onboarding;
import org.sopt.confeti.global.common.BaseResponse;
import org.sopt.confeti.global.interceptor.auth.UserContext;
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
@RequestMapping("/user/onboard")
@Validated
public class UserOnboardController implements UserOnboardControllerDocs {

    private final UserOnboardFacade userOnboardFacade;

    @Deprecated
    @Onboarding
    @GetMapping("/artists/{artistId}/related")
    public ResponseEntity<BaseResponse<UserOnboardRelatedArtistsResponse>> getRelatedArtists(
        @PathVariable String artistId,
        @RequestParam(defaultValue = "1") @Min(1) @Max(30) Integer limit
    ) {
        UserOnboardRelatedArtistsDTO relatedArtists = userOnboardFacade.getRelatedArtists(
            UserContext.get().id(),
            artistId, limit);
        return ApiResponseUtil.success(SuccessMessage.SUCCESS,
            UserOnboardRelatedArtistsResponse.from(relatedArtists));
    }

    @Onboarding
    @GetMapping("/artists/search")
    public ResponseEntity<BaseResponse<UserOnboardRelatedArtistsResponse>> getArtistsRelatedTerm(
        @RequestParam String term,
        @RequestParam(defaultValue = "1") @Min(1) @Max(25) Integer limit
    ) {
        UserOnboardRelatedArtistsDTO relatedArtistsDTO = userOnboardFacade.getArtistsRelatedTerm(
            term, limit);
        return ApiResponseUtil.success(SuccessMessage.SUCCESS,
            UserOnboardRelatedArtistsResponse.from(relatedArtistsDTO));
    }

    @ApiVersion("2")
    @Onboarding
    @GetMapping("/artists")
    public ResponseEntity<BaseResponse<UserOnboardArtistsResponse>> getOnboardArtists(
        @RequestParam(required = false, defaultValue = "50") int limit,
        @RequestParam(required = false) String targetArtistId
    ) {
        UserOnboardArtistsDTO onboardArtists = userOnboardFacade.getOnboardArtists(limit,
            Optional.ofNullable(targetArtistId));
        return ApiResponseUtil.success(SuccessMessage.SUCCESS,
            UserOnboardArtistsResponse.from(onboardArtists));
    }

    @Deprecated
    @Onboarding
    @GetMapping("/artists")
    public ResponseEntity<BaseResponse<UserOnboardTopArtistsResponse>> getTopArtists(
        @RequestParam(required = false, defaultValue = "100") @Min(1) @Max(200) int limit
    ) {
        UserOnboardTopArtistsDTO topArtists = userOnboardFacade.getTopArtists(limit);
        userOnboardFacade.cacheTopArtistsToUser(topArtists);
        return ApiResponseUtil.success(SuccessMessage.SUCCESS,
            UserOnboardTopArtistsResponse.from(topArtists));
    }

    @Onboarding
    @PostMapping("/artists/{artistId}")
    public ResponseEntity<BaseResponse<Void>> addArtist(
        @PathVariable String artistId
    ) {
        userOnboardFacade.cacheExposedArtist(artistId);
        return ApiResponseUtil.success(SuccessMessage.SUCCESS);
    }

    @Onboarding
    @GetMapping("/status")
    public ResponseEntity<BaseResponse<GetOnboardStatusResponse>> getOnboardStatus() {
        GetOnboardStatusDTO onboardStatusDTO = userOnboardFacade.getOnboardStatus();
        return ApiResponseUtil.success(SuccessMessage.SUCCESS,
            GetOnboardStatusResponse.from(onboardStatusDTO));
    }

    @Onboarding
    @GetMapping("/artists/favorite")
    public ResponseEntity<BaseResponse<UserOnboardFavoriteArtistsResponse>> getFavoriteArtists() {
        UserOnboardFavoriteArtistsDTO favoriteArtists = userOnboardFacade.getFavoriteArtists();
        return ApiResponseUtil.success(SuccessMessage.SUCCESS,
            UserOnboardFavoriteArtistsResponse.from(favoriteArtists));
    }

    @Onboarding
    @PatchMapping("/artists/favorite")
    public ResponseEntity<BaseResponse<Void>> patchFavoriteArtists(
        @RequestBody PatchOnboardFavoriteArtistsRequest request
    ) {
        userOnboardFacade.patchFavoriteArtist(request.toDto());
        return ApiResponseUtil.success(SuccessMessage.SUCCESS);
    }

    @Onboarding
    @PostMapping("/artists/favorite")
    public ResponseEntity<BaseResponse<UserOnboardFavoriteArtistsResponse>> addFavoriteArtists(
        @RequestBody AddOnboardFavoriteArtistRequest request
    ) {
        UserOnboardFavoriteArtistsDTO favoriteArtists = userOnboardFacade.addFavoriteArtists(
            request.toDTO());
        return ApiResponseUtil.success(SuccessMessage.SUCCESS,
            UserOnboardFavoriteArtistsResponse.from(favoriteArtists));
    }

    @Onboarding
    @PostMapping
    public ResponseEntity<BaseResponse<Void>> onboard() {
        userOnboardFacade.onboard();
        userOnboardFacade.flushCachedOnboardArtists();
        return ApiResponseUtil.success(SuccessMessage.SUCCESS);
    }
}
