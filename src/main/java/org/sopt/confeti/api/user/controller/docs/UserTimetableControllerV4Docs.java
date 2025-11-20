package org.sopt.confeti.api.user.controller.docs;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.sopt.confeti.api.user.dto.request.AddTimetableFestivalRequest;
import org.sopt.confeti.api.user.dto.request.PatchTimetableFestivalRequest;
import org.sopt.confeti.global.annotation.UserId;
import org.sopt.confeti.global.common.BaseResponse;
import org.sopt.confeti.global.common.swagger.AuthErrorResponses;
import org.sopt.confeti.global.common.swagger.CommonErrorResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Tag(name = "타임테이블")
public interface UserTimetableControllerV4Docs {

    @Operation(summary = "타임테이블 페스티벌 추가")
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
    ResponseEntity<BaseResponse<Void>> addTimetableFestival(
        @UserId Long userId,
        @RequestBody AddTimetableFestivalRequest addTimetableFestivalRequest
    );

    @Operation(summary = "타임테이블 페스티벌 다건 삭제")
    @ApiResponses(
        value = {
            @ApiResponse(
                responseCode = "200",
                description = "성공"
            )
        }
    )
    @PatchMapping("/festivals")
    ResponseEntity<BaseResponse<Void>> updateTimetableFestival(
        @UserId Long userId,
        @RequestBody PatchTimetableFestivalRequest patchTimetableFestivalRequest
    );
}
