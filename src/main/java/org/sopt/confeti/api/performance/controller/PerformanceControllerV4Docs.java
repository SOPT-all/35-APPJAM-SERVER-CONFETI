package org.sopt.confeti.api.performance.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import org.sopt.confeti.api.performance.dto.response.PerformancesRecommendResponse;
import org.sopt.confeti.api.performance.dto.response.RecentPerformancesResponse;
import org.sopt.confeti.api.performance.dto.response.RecommendPerformancesResponse;
import org.sopt.confeti.global.annotation.UserId;
import org.sopt.confeti.global.common.BaseResponse;
import org.sopt.confeti.global.common.swagger.CommonErrorResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestParam;

@Tag(name = "공연")
public interface PerformanceControllerV4Docs {

    @Operation(summary = "Confeti's pick 추천 공연 조회")
    @ApiResponses(
        value = {
            @ApiResponse(
                responseCode = "200",
                description = "성공",
                content =
                @Content(
                    schema =
                    @Schema(
                        implementation = RecommendPerformancesResponse.class
                    )
                )
            )
        }
    )
    @CommonErrorResponses
    ResponseEntity<BaseResponse<RecommendPerformancesResponse>> getRecommendPerformances(
        @RequestParam(defaultValue = "5") @Min(1) @Max(20) int limit
    );

    @Operation(
        summary = "공연 미리듣기 조회",
        description =
            """
                V4 변경사항
                - 대상 공연 : 유저가 좋아요 누른 예정된 공연 또는 예정된 전체 공연
                - 랜덤한 예정된 공연 3개, 각 공연 당 랜덤한 음악 3개를 조회
                - 공연이 3개 이하일 수도, 음악이 3개 이하일 수도 있음
                - 아예 조회된 값이 없을 수도 있음        
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
    ResponseEntity<BaseResponse<PerformancesRecommendResponse>> getSongRecommend(
        @UserId(require = false) Long userId,
        @RequestParam(defaultValue = "3") @Min(1) @Max(5) Integer performanceLimit,
        @RequestParam(defaultValue = "3") @Min(1) @Max(5) Integer songLimit
    );

    @Operation(
        summary = "최신 공연 등록순 조회",
        description =
            """
                V4 변경사항
                - 장소 응답 값 추가  
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
    ResponseEntity<BaseResponse<RecentPerformancesResponse>> getRecentPerformances(
        @UserId(require = false) Long userId
    );
}
