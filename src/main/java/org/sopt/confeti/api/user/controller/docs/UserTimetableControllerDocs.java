package org.sopt.confeti.api.user.controller.docs;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import org.sopt.confeti.api.user.dto.request.timetable.AddTimetablesRequest;
import org.sopt.confeti.api.user.dto.request.timetable.PatchTimeBlocksRequest;
import org.sopt.confeti.api.user.dto.request.timetable.PatchTimetablesRequest;
import org.sopt.confeti.api.user.dto.response.timetable.TimetableCursorResponse;
import org.sopt.confeti.api.user.dto.response.timetable.TimetableDatesResponse;
import org.sopt.confeti.api.user.dto.response.timetable.TimetableEntireFestivalResponse;
import org.sopt.confeti.api.user.dto.response.timetable.TimetableFestivalResponse;
import org.sopt.confeti.api.user.dto.response.timetable.TimetableHistoryResponse;
import org.sopt.confeti.api.user.dto.response.timetable.TimetablesPreviewResponse;
import org.sopt.confeti.api.user.dto.response.timetable.TimetablesToAddResponse;
import org.sopt.confeti.global.common.BaseResponse;
import org.sopt.confeti.global.common.constant.Default;
import org.sopt.confeti.global.common.constant.PerformanceStatus;
import org.sopt.confeti.global.common.constant.RequestConstraint;
import org.sopt.confeti.global.common.constant.TimetableSortType;
import org.sopt.confeti.global.common.swagger.AuthErrorResponses;
import org.sopt.confeti.global.common.swagger.CommonErrorResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@Tag(name = "유저 온보딩")
public interface UserTimetableControllerDocs {

    @Operation(summary = "타임테이블 추가 이력 조회")
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
    ResponseEntity<BaseResponse<TimetableHistoryResponse>> getHasTimetableHistory();

    @Operation(summary = "추가할 페스티벌 목록 조회")
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
    ResponseEntity<BaseResponse<TimetablesToAddResponse>> getTimetablesToAdd(
        @RequestParam(name = "cursor", required = false) Long cursor
    );

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
        @Valid @RequestBody AddTimetablesRequest addTimetablesRequest
    );

    @Operation(summary = "등록된 시간표 조회")
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
    ResponseEntity<BaseResponse<TimetableFestivalResponse>> getTimetableFestival(
        @PathVariable(name = "timetableId") @Min(RequestConstraint.ID) long timetableId,
        @PathVariable(name = "festivalDateId") @Min(RequestConstraint.ID) long festivalDateId
    );

    @Operation(summary = "시간표 수정")
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
    ResponseEntity<BaseResponse<Void>> updateTimetableFestival(
        @PathVariable(name = "timetableId") @Min(RequestConstraint.ID) long timetableId,
        @Valid @RequestBody PatchTimeBlocksRequest patchTimeBlocksRequest
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
    ResponseEntity<BaseResponse<TimetableCursorResponse>> getTimetables(
        @RequestParam(required = false) TimetableSortType sortBy,
        @RequestParam(required = false) String cursor,
        @RequestParam(defaultValue = Default.PERFORMANCE_STATUS) PerformanceStatus status
    );

    @Operation(summary = "타임테이블 미리보기 조회")
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
    ResponseEntity<BaseResponse<TimetablesPreviewResponse>> getTimetablesPreview();

    @Operation(summary = "지난 페스티벌 정보 조회")
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
    ResponseEntity<BaseResponse<TimetableEntireFestivalResponse>> getEntireFestivalInfo(
        @PathVariable(name = "timetableId") @Min(RequestConstraint.ID) Long timetableId
    );

    @Operation(summary = "지난 페스티벌 날짜별 타임테이블 조회")
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
    ResponseEntity<BaseResponse<TimetableFestivalResponse>> getEntireFestivalDateInfo(
        @PathVariable(name = "timetableId") @Min(RequestConstraint.ID) Long timetableId,
        @PathVariable(name = "festivalDateId") @Min(RequestConstraint.ID) Long festivalDateId
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
    @AuthErrorResponses
    @CommonErrorResponses
    ResponseEntity<BaseResponse<Void>> updateTimetableFestival(
        @Valid @RequestBody PatchTimetablesRequest patchTimetablesRequest
    );

    @Operation(summary = "타임테이블 날짜 목록 조회")
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
    ResponseEntity<BaseResponse<TimetableDatesResponse>> getTimetableDates(
        @PathVariable Long timetableId
    );
}
