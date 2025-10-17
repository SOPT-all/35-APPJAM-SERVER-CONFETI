package org.sopt.confeti.api.performance.controller.docs;

import io.swagger.v3.oas.annotations.Operation;
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

    @Operation(
            summary = "Confeti's pick 추천 공연 조회",
            description =
                    """
                    V4 변경사항
                    - 공연 개수를 클라이언트에서 정할 수 있도록 수정 (1 ~ 20, default 5)
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
    ResponseEntity<BaseResponse<RecommendPerformancesResponse>> getRecommendPerformances(
        @RequestParam(defaultValue = "5") @Min(1) @Max(20) int limit
    );
}
