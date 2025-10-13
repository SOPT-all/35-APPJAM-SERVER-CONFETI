package org.sopt.confeti.api.performance.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import org.sopt.confeti.api.performance.dto.response.RecommendPerformancesResponse;
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
    ResponseEntity<BaseResponse<?>> getRecommendPerformances(
            @RequestParam(required = false, defaultValue = "5") @Min(1) @Max(20) int limit
    );
}
