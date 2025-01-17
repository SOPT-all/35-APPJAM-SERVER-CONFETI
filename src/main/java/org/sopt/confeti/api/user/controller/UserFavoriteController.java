package org.sopt.confeti.api.user.controller;

import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.sopt.confeti.api.user.dto.response.UserFavoriteResponse;
import org.sopt.confeti.api.user.facade.UserFavoriteFacade;
import org.sopt.confeti.api.user.facade.dto.response.UserFavoriteArtistDTO;
import org.sopt.confeti.global.common.BaseResponse;
import org.sopt.confeti.global.message.SuccessMessage;
import org.sopt.confeti.global.util.ApiResponseUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping("/user/favorites")
public class UserFavoriteController {

    private final UserFavoriteFacade userFavoriteFacade;

    @PostMapping("/festivals/{festivalId}")
    public ResponseEntity<BaseResponse<?>> postFavoriteFestival(@RequestHeader("Authorization") Long userId, @PathVariable
                                                                @Min(value = 0, message = "요청 형식이 올바르지 않습니다.") Long festivalId) {
        userFavoriteFacade.save(userId, festivalId);
        return ApiResponseUtil.success(SuccessMessage.SUCCESS);
    }

    @DeleteMapping("/festivals/{festivalId}")
    public ResponseEntity<BaseResponse<?>> deleteFavoriteFestival(@RequestHeader("Authorization") Long userId, @PathVariable
                                                                  @Min(value = 0, message = "요청 형식이 올바르지 않습니다.") Long festivalId) {
        userFavoriteFacade.delete(userId, festivalId);
        return ApiResponseUtil.success(SuccessMessage.SUCCESS);
    }

    @GetMapping("/artists")
    public ResponseEntity<BaseResponse<?>> getFavoriteArtists(@RequestHeader("Authorization") Long userId) {
        UserFavoriteArtistDTO userFavoriteArtistDTO = userFavoriteFacade.getArtistList(userId);
        return ApiResponseUtil.success(SuccessMessage.SUCCESS, UserFavoriteResponse.of(userFavoriteArtistDTO.artists()));
    }
}
