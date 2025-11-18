package org.sopt.confeti.api.user.controller.docs;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.sopt.confeti.api.user.dto.request.AddTimetableFestivalRequest;
import org.sopt.confeti.api.user.dto.response.UserTimetableCursorResponse;
import org.sopt.confeti.global.annotation.UserId;
import org.sopt.confeti.global.common.BaseResponse;
import org.sopt.confeti.global.common.constant.Default;
import org.sopt.confeti.global.common.constant.PerformanceStatus;
import org.sopt.confeti.global.common.constant.TimetableSortType;
import org.sopt.confeti.global.common.swagger.AuthErrorResponses;
import org.sopt.confeti.global.common.swagger.CommonErrorResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

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

    @Operation(
            summary = "타임테이블에 등록된 페스티벌 목록 조회",
            description =
                    """
                        추가 설명
                        - status에는 upcoming, all 값이 있음. 기본 값은 upcoming.
                        - upcoming은 예정된 공연으로 아직 종료되지 않은 공연을 대상으로 조회.
                        - all은 모든 공연으로 종료 여부와 관계없이 모두 조회.
                        - orderBy에는 latest, earliest 값이 있음. 기본 값은 earliest.
                        - latest는 공연 시작일의 내림차순 (늦은 순, 2025-10-10 > 2025-10-09 > ...)
                        - earliest는 공연 시작일의 오름차순 (최신 순, 2025-10-09 > 2025-10-10 > ...)
                        - cursor는 Base64 인코딩된 문자열이다. 응답으로 오는 nextCursor 값을 그대로 전달하면 된다.
                        - 단, nextCursor가 empty일 경우 더 이상 조회할 공연이 없다는 뜻이므로 조회를 하면 안된다.
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
    ResponseEntity<BaseResponse<UserTimetableCursorResponse>> getTimetables(
            @UserId Long userId,
            @RequestParam(defaultValue = Default.TIMETABLE_SORT_TYPE) TimetableSortType sortBy,
            @RequestParam(required = false) String cursor,
            @RequestParam(defaultValue = Default.PERFORMANCE_STATUS) PerformanceStatus status
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
