package org.sopt.confeti.api.user.controller;

import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.sopt.confeti.api.user.controller.docs.UserTimetableControllerDocs;
import org.sopt.confeti.api.user.dto.request.timetable.AddTimetablesRequest;
import org.sopt.confeti.api.user.dto.request.timetable.PatchTimeBlocksRequest;
import org.sopt.confeti.api.user.dto.request.timetable.PatchTimetablesRequest;
import org.sopt.confeti.api.user.dto.response.timetable.TimetableCreateResponse;
import org.sopt.confeti.api.user.dto.response.timetable.TimetableCursorResponse;
import org.sopt.confeti.api.user.dto.response.timetable.TimetableDatesResponse;
import org.sopt.confeti.api.user.dto.response.timetable.TimetableEntireFestivalResponse;
import org.sopt.confeti.api.user.dto.response.timetable.TimetableExistenceResponse;
import org.sopt.confeti.api.user.dto.response.timetable.TimetableFestivalResponse;
import org.sopt.confeti.api.user.dto.response.timetable.TimetableHistoryResponse;
import org.sopt.confeti.api.user.dto.response.timetable.TimetablesPreviewResponse;
import org.sopt.confeti.api.user.dto.response.timetable.TimetablesToAddResponse;
import org.sopt.confeti.api.user.facade.UserTimetableFacade;
import org.sopt.confeti.api.user.facade.dto.request.timetable.AddTimetablesDTO;
import org.sopt.confeti.api.user.facade.dto.request.timetable.PatchTimeBlocksDTO;
import org.sopt.confeti.api.user.facade.dto.response.timetable.TimetableCreateResponseDTO;
import org.sopt.confeti.api.user.facade.dto.response.timetable.TimetableDatesDTO;
import org.sopt.confeti.api.user.facade.dto.response.timetable.TimetableEntireFestivalDTO;
import org.sopt.confeti.api.user.facade.dto.response.timetable.TimetableExistenceDTO;
import org.sopt.confeti.api.user.facade.dto.response.timetable.TimetableFestivalBasicDTO;
import org.sopt.confeti.api.user.facade.dto.response.timetable.TimetableHistoryDTO;
import org.sopt.confeti.api.user.facade.dto.response.timetable.TimetableToAddDTO;
import org.sopt.confeti.api.user.facade.dto.response.timetable.TimetablesDTO;
import org.sopt.confeti.domain.timetable.Timetable;
import org.sopt.confeti.domain.timetable.TimetableCursor;
import org.sopt.confeti.domain.timetable.TimetableCursor.CursorData;
import org.sopt.confeti.domain.user.constant.Role;
import org.sopt.confeti.global.annotation.ApiVersion;
import org.sopt.confeti.global.annotation.Permission;
import org.sopt.confeti.global.common.BaseResponse;
import org.sopt.confeti.global.common.CursorPage;
import org.sopt.confeti.global.common.constant.Default;
import org.sopt.confeti.global.common.constant.PerformanceStatus;
import org.sopt.confeti.global.common.constant.RequestConstraint;
import org.sopt.confeti.global.common.constant.TimetableSortType;
import org.sopt.confeti.global.message.SuccessMessage;
import org.sopt.confeti.global.util.ApiResponseUtil;
import org.sopt.confeti.global.util.S3FileHandler;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Validated
@RequiredArgsConstructor
@RequestMapping("/user/timetables")
public class UserTimetableController implements UserTimetableControllerDocs {

    private final UserTimetableFacade userTimetableFacade;
    private final S3FileHandler s3FileHandler;

    @Permission(role = {Role.GENERAL})
    @GetMapping("/history")
    public ResponseEntity<BaseResponse<TimetableHistoryResponse>> getHasTimetableHistory() {
        TimetableHistoryDTO timetableHistoryDTO = userTimetableFacade.getHasTimetableHistory();
        return ApiResponseUtil.success(SuccessMessage.SUCCESS,
            TimetableHistoryResponse.from(timetableHistoryDTO));
    }

    @Permission(role = {Role.GENERAL})
    @GetMapping("/exists")
    public ResponseEntity<BaseResponse<TimetableExistenceResponse>> getTimetableExistence(
        @RequestParam(name = "festivalId") @Min(RequestConstraint.ID) Long festivalId
    ) {
        TimetableExistenceDTO timetableExistenceDTO = userTimetableFacade.getTimetableExistence(
            festivalId);
        return ApiResponseUtil.success(SuccessMessage.SUCCESS,
            TimetableExistenceResponse.from(timetableExistenceDTO));
    }

    @Permission(role = {Role.GENERAL})
    @GetMapping("/festivals")
    public ResponseEntity<BaseResponse<TimetablesToAddResponse>> getTimetablesToAdd(
        @RequestParam(name = "cursor", required = false) Long cursor
    ) {
        CursorPage<TimetableToAddDTO> timetablesToAdd = userTimetableFacade.getTimetablesToAdd(
            cursor);
        return ApiResponseUtil.success(SuccessMessage.SUCCESS,
            TimetablesToAddResponse.of(timetablesToAdd, s3FileHandler));
    }

    @Permission(role = {Role.GENERAL})
    @PostMapping
    public ResponseEntity<BaseResponse<TimetableCreateResponse>> addTimetableFestival(
        @RequestBody AddTimetablesRequest addTimetablesRequest
    ) {
        TimetableCreateResponseDTO response = userTimetableFacade.addTimetables(
            AddTimetablesDTO.from(addTimetablesRequest));
        return ApiResponseUtil.success(SuccessMessage.SUCCESS,
            TimetableCreateResponse.from(response));
    }

    @Permission(role = {Role.GENERAL})
    @GetMapping("/{timetableId}/dates/{festivalDateId}")
    public ResponseEntity<BaseResponse<TimetableFestivalResponse>> getTimetableFestival(
        @PathVariable(name = "timetableId") long timetableId,
        @PathVariable(name = "festivalDateId") long festivalDateId
    ) {
        TimetableFestivalBasicDTO response = userTimetableFacade.getTimetableInfo(
            timetableId, festivalDateId);
        return ApiResponseUtil.success(SuccessMessage.SUCCESS,
            TimetableFestivalResponse.from(response));
    }

    @Permission(role = {Role.GENERAL})
    @PatchMapping("/{timetableId}/time-blocks")
    public ResponseEntity<BaseResponse<Void>> updateTimetableFestival(
        @PathVariable(name = "timetableId") long timetableId,
        @RequestBody PatchTimeBlocksRequest patchTimeBlocksRequest
    ) {
        userTimetableFacade.patchTimeBlocks(timetableId,
            PatchTimeBlocksDTO.from(patchTimeBlocksRequest));
        return ApiResponseUtil.success(SuccessMessage.SUCCESS);
    }

    @ApiVersion("2")
    @GetMapping
    @Permission(role = {Role.GENERAL})
    public ResponseEntity<BaseResponse<TimetableCursorResponse>> getTimetables(
        @RequestParam(required = false) TimetableSortType sortBy,
        @RequestParam(required = false) String cursor,
        @RequestParam(defaultValue = Default.PERFORMANCE_STATUS) PerformanceStatus status
    ) {
        CursorData cursorData = TimetableCursor.decode(cursor);
        CursorPage<Timetable> timetableCursorPage = userTimetableFacade.getTimetableCursorPage(
            sortBy, cursorData, status);
        return ApiResponseUtil.success(SuccessMessage.SUCCESS,
            TimetableCursorResponse.of(timetableCursorPage, s3FileHandler));
    }

    @Permission(role = {Role.GENERAL})
    @GetMapping("/preview")
    public ResponseEntity<BaseResponse<TimetablesPreviewResponse>> getTimetablesPreview() {
        TimetablesDTO timetables = userTimetableFacade.getTimetablesPreview();
        return ApiResponseUtil.success(SuccessMessage.SUCCESS,
            TimetablesPreviewResponse.of(timetables, s3FileHandler));
    }

    @Permission(role = {Role.GENERAL})
    @GetMapping("/{timetableId}/archive")
    public ResponseEntity<BaseResponse<TimetableEntireFestivalResponse>> getEntireFestivalInfo(
        @PathVariable(name = "timetableId") Long timetableId
    ) {
        TimetableEntireFestivalDTO entireFestivalDTO = userTimetableFacade.getEntireFestivalInfo(
            timetableId);
        return ApiResponseUtil.success(SuccessMessage.SUCCESS,
            TimetableEntireFestivalResponse.of(entireFestivalDTO, s3FileHandler));
    }

    @Permission(role = {Role.GENERAL})
    @GetMapping("/{timetableId}/dates/{festivalDateId}/archive")
    public ResponseEntity<BaseResponse<TimetableFestivalResponse>> getEntireFestivalDateInfo(
        @PathVariable(name = "timetableId") Long timetableId,
        @PathVariable(name = "festivalDateId") Long festivalDateId
    ) {
        TimetableFestivalBasicDTO festivalBasicDTO = userTimetableFacade.getEntireFestivalDateInfo(
            timetableId, festivalDateId);
        return ApiResponseUtil.success(SuccessMessage.SUCCESS,
            TimetableFestivalResponse.from(festivalBasicDTO));
    }

    @ApiVersion("2")
    @Permission(role = {Role.GENERAL})
    @PatchMapping
    public ResponseEntity<BaseResponse<Void>> updateTimetableFestival(
        @RequestBody PatchTimetablesRequest patchTimetablesRequest
    ) {
        userTimetableFacade.updateTimetables(patchTimetablesRequest.toCommand());
        return ApiResponseUtil.success(SuccessMessage.SUCCESS);
    }

    @Permission(role = {Role.GENERAL})
    @GetMapping("/{timetableId}/dates")
    public ResponseEntity<BaseResponse<TimetableDatesResponse>> getTimetableDates(
        @PathVariable Long timetableId
    ) {
        TimetableDatesDTO timetableDates = userTimetableFacade.getTimetableDates(
            timetableId);
        return ApiResponseUtil.success(SuccessMessage.SUCCESS,
            TimetableDatesResponse.of(timetableDates, s3FileHandler));
    }
}
