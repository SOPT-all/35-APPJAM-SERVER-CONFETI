package org.sopt.confeti.api.setlist.controller.docs;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import org.sopt.confeti.api.setlist.dto.response.GetAllSetlistsResponse;
import org.sopt.confeti.api.setlist.dto.response.SetlistSummaryResponse;
import org.sopt.confeti.domain.setlist.SetlistSortType;
import org.sopt.confeti.global.annotation.UserId;
import org.sopt.confeti.global.common.BaseResponse;
import org.sopt.confeti.global.common.swagger.AuthErrorResponses;
import org.sopt.confeti.global.common.swagger.CommonErrorResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Tag(name = "My 셋리스트")
public interface SetlistControllerV4Docs {

    @Operation(summary = "MY 셋리스트 > 공연 전체보기 목록 조회")
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
    @GetMapping("/all")
    ResponseEntity<BaseResponse<GetAllSetlistsResponse>> getAllMySetlists(
        @UserId Long userId,
        @RequestParam(required = false) SetlistSortType sortBy
    );

    @Operation(summary = "MY 셋리스트 > 공연 미리보기 목록 조회")
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
    @GetMapping("/preview")
    ResponseEntity<BaseResponse<List<SetlistSummaryResponse>>> getPreviewMySetlists(
        @UserId Long userId
    );
}
