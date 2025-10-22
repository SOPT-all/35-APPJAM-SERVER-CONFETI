package org.sopt.confeti.api.user.controller.docs;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.sopt.confeti.api.user.dto.request.PatchOnboardFavoriteArtistsRequest;
import org.sopt.confeti.api.user.dto.response.onboard.UserOnboardFavoriteArtistsResponse;
import org.sopt.confeti.global.annotation.UserId;
import org.sopt.confeti.global.common.BaseResponse;
import org.sopt.confeti.global.common.swagger.AuthErrorResponses;
import org.sopt.confeti.global.common.swagger.CommonErrorResponses;
import org.springframework.http.ResponseEntity;

@Tag(name = "유저 온보딩")
public interface UserOnboardControllerV4Docs {

    @Operation(summary = "온보딩 진행 중 favorite 으로 선택했던 아티스트 목록 조회")
    @ApiResponses(
        value = {
            @ApiResponse(
                responseCode = "200",
                description = "성공"
            )
        }
    )
    @AuthErrorResponses
    @CommonErrorResponses
    ResponseEntity<BaseResponse<UserOnboardFavoriteArtistsResponse>> getFavoriteArtists(
        Long userId
    );

    @Operation(
        summary = "회원가입한 유저의 온보딩 완료 요청",
        description =
            """
                V4 변경사항
                - RequestBody 로 favoriteArtistId 목록을 받아서 처리하던 부분이 사라짐
                - 서버에서 내부적으로 온보딩 시 사용된 favoriteArtistId 를 관리하므로 해당 데이터를 사용해 온보딩 완료 로직을 진행함
                """
    )
    @ApiResponses(
        value = {
            @ApiResponse(
                responseCode = "200",
                description = "성공"
            )
        }
    )
    @CommonErrorResponses
    @AuthErrorResponses
    ResponseEntity<BaseResponse<Void>> onboard(
        @UserId Long userId
    );

    @Operation(
        summary = "온보딩 진행 중 favorite 으로 선택했던 아티스트 목록 수정",
        description =
            """
                V4 기준, 해당 API 는 선택했던 아티스트 목록 중 원하는 아티스트들을 id값으로 삭제하는 것만 가능함
                """
    )
    @ApiResponses(
        value = {
            @ApiResponse(
                responseCode = "200",
                description = "성공"
            )
        }
    )
    @AuthErrorResponses
    @CommonErrorResponses
    ResponseEntity<BaseResponse<Void>> patchFavoriteArtists(
        Long userId,
        PatchOnboardFavoriteArtistsRequest request
    );
}
