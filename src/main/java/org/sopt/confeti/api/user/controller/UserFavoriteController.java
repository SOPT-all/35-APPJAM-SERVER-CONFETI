package org.sopt.confeti.api.user.controller;

import jakarta.validation.constraints.Min;
import java.util.Collections;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.sopt.confeti.api.user.dto.response.UpcomingPerformanceResponse;
import org.sopt.confeti.api.user.dto.response.UserFavoriteArtistsPreviewResponse;
import org.sopt.confeti.api.user.dto.response.UserFavoriteArtistsResponse;
import org.sopt.confeti.api.user.dto.response.UserFavoritePerformancesAllResponse;
import org.sopt.confeti.api.user.dto.response.UserFavoritePerformancesResponse;
import org.sopt.confeti.api.user.facade.UserFavoriteFacade;
import org.sopt.confeti.api.user.facade.dto.response.UpcomingPerformanceDTO;
import org.sopt.confeti.api.user.facade.dto.response.UserFavoriteArtistsDTO;
import org.sopt.confeti.api.user.facade.dto.response.UserFavoriteArtistsPreviewDTO;
import org.sopt.confeti.api.user.facade.dto.response.UserFavoritePerformancesAllDTO;
import org.sopt.confeti.api.user.facade.dto.response.UserFavoritePerformancesDTO;
import org.sopt.confeti.domain.user.constant.Role;
import org.sopt.confeti.global.annotation.Permission;
import org.sopt.confeti.global.annotation.UserId;
import org.sopt.confeti.global.common.BaseResponse;
import org.sopt.confeti.global.common.constant.RequestConstraint;
import org.sopt.confeti.global.message.SuccessMessage;
import org.sopt.confeti.global.util.ApiResponseUtil;
import org.sopt.confeti.global.util.S3FileHandler;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping("/user/favorites")
public class UserFavoriteController {

    private final UserFavoriteFacade userFavoriteFacade;
    private final S3FileHandler s3FileHandler;

    @Permission(role = {Role.GENERAL})
    @PostMapping("/festivals/{festivalId}")
    public ResponseEntity<BaseResponse<?>> postFavoriteFestival(
            @UserId Long userId,
            @PathVariable(name = "festivalId") @Min(RequestConstraint.ID) Long festivalId) {
        userFavoriteFacade.addFestivalFavorite(userId, festivalId);
        return ApiResponseUtil.success(SuccessMessage.SUCCESS);
    }

    @Permission(role = {Role.GENERAL})
    @DeleteMapping("/festivals/{festivalId}")
    public ResponseEntity<BaseResponse<?>> deleteFavoriteFestival(
            @UserId Long userId,
            @PathVariable(name = "festivalId") @Min(RequestConstraint.ID) Long festivalId) {
        userFavoriteFacade.removeFestivalFavorite(userId, festivalId);
        return ApiResponseUtil.success(SuccessMessage.SUCCESS);
    }

    @Permission(role = {Role.GENERAL})
    @GetMapping("/artists/preview")
    public ResponseEntity<BaseResponse<?>> getFavoriteArtistsPreview(
            @UserId Long userId
    ) {
        UserFavoriteArtistsPreviewDTO userFavoriteArtistsPreviewDTO = userFavoriteFacade.getFavoriteArtistsPreview(
                userId);
        return ApiResponseUtil.success(SuccessMessage.SUCCESS,
                UserFavoriteArtistsPreviewResponse.from(userFavoriteArtistsPreviewDTO.artists()));
    }

    @Permission(role = {Role.GENERAL})
    @PostMapping("/artists/{artistId}")
    public ResponseEntity<BaseResponse<?>> addArtistFavorite(
            @UserId Long userId,
            @PathVariable(name = "artistId") String artistId
    ) {
        userFavoriteFacade.addArtistFavorite(userId, artistId);
        return ApiResponseUtil.success(SuccessMessage.SUCCESS);
    }

    @Permission(role = {Role.GENERAL})
    @DeleteMapping("/artists/{artistId}")
    public ResponseEntity<BaseResponse<?>> removeArtistFavorite(
            @UserId Long userId,
            @PathVariable(name = "artistId") String artistId
    ) {
        userFavoriteFacade.removeArtistFavorite(userId, artistId);
        return ApiResponseUtil.success(SuccessMessage.SUCCESS);
    }

    @Permission(role = {Role.GENERAL})
    @PostMapping("/concerts/{concertId}")
    public ResponseEntity<BaseResponse<?>> addConcertFavorite(
            @UserId Long userId,
            @PathVariable(name = "concertId") Long concertId
    ) {
        userFavoriteFacade.addConcertFavorite(userId, concertId);
        return ApiResponseUtil.success(SuccessMessage.SUCCESS);
    }

    @Permission(role = {Role.GENERAL})
    @DeleteMapping("/concerts/{concertId}")
    public ResponseEntity<BaseResponse<?>> removeConcertFavorite(
            @UserId Long userId,
            @PathVariable(name = "concertId") Long concertId
    ) {
        userFavoriteFacade.removeConcertFavorite(userId, concertId);
        return ApiResponseUtil.success(SuccessMessage.SUCCESS);
    }

    @Permission(role = {Role.GENERAL})
    @GetMapping("/performances/preview")
    public ResponseEntity<BaseResponse<?>> getFavoritePerformances(
            @UserId Long userId
    ) {
        UserFavoritePerformancesDTO userFavoritePerformancesDTO = userFavoriteFacade.getFavoritePerformances(userId);
        return ApiResponseUtil.success(SuccessMessage.SUCCESS,
                UserFavoritePerformancesResponse.of(userFavoritePerformancesDTO, s3FileHandler));
    }

    @Permission(role = {Role.GENERAL})
    @GetMapping("/performances")
    public ResponseEntity<BaseResponse<?>> getFavoritePerformancesAll(
            @RequestParam(value = "type") String type,
            @UserId Long userId
    ) {
        UserFavoritePerformancesAllDTO userFavoritePerformancesAllDTO = userFavoriteFacade.getFavoritePerformancesAll(
                userId, type);
        return ApiResponseUtil.success(SuccessMessage.SUCCESS,
                UserFavoritePerformancesAllResponse.of(userFavoritePerformancesAllDTO, s3FileHandler));
    }

    @Permission(role = {Role.GENERAL})
    @GetMapping("/performance")
    public ResponseEntity<BaseResponse<?>> getUpcomingPerformance(
            @UserId Long userId
    ) {
        Optional<UpcomingPerformanceDTO> upcomingPerformanceDTO = userFavoriteFacade.getUpcomingPerformance(userId);
        return upcomingPerformanceDTO
                .map(performanceDTO ->
                        ApiResponseUtil.success(SuccessMessage.SUCCESS, UpcomingPerformanceResponse.of(performanceDTO, s3FileHandler)))
                .orElseGet(() -> ApiResponseUtil.success(SuccessMessage.SUCCESS, Collections.emptyMap()));
    }

    @Permission(role = {Role.GENERAL})
    @GetMapping("/artists")
    public ResponseEntity<BaseResponse<?>> getFavoriteArtists(
            @UserId Long userId,
            @RequestParam(value = "sortBy", defaultValue = "createdAt") String sortBy
    ) {
        UserFavoriteArtistsDTO userFavoriteArtistsDTO = userFavoriteFacade.getFavoriteArtists(userId, sortBy);
        return ApiResponseUtil.success(SuccessMessage.SUCCESS,
                UserFavoriteArtistsResponse.from(userFavoriteArtistsDTO.artists()));
    }
}
