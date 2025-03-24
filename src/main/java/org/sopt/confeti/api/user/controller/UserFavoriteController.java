package org.sopt.confeti.api.user.controller;

import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.sopt.confeti.api.user.dto.response.UserFavoritePerformancesResponse;
import org.sopt.confeti.api.user.dto.response.UserFavoriteResponse;
import org.sopt.confeti.api.user.facade.UserFavoriteFacade;
import org.sopt.confeti.api.user.facade.dto.response.UserFavoriteArtistDTO;
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
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<BaseResponse<?>> getFavoriteArtists(
            @UserId Long userId
    ) {
        UserFavoriteArtistDTO userFavoriteArtistDTO = userFavoriteFacade.getArtistListPreview(userId);
        return ApiResponseUtil.success(SuccessMessage.SUCCESS, UserFavoriteResponse.from(userFavoriteArtistDTO.artists()));
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
        return ApiResponseUtil.success(SuccessMessage.SUCCESS, UserFavoritePerformancesResponse.of(userFavoritePerformancesDTO, s3FileHandler));
    }
}
