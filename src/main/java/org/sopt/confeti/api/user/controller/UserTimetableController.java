package org.sopt.confeti.api.user.controller;

import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.sopt.confeti.api.user.controller.docs.UserTimetableControllerDocs;
import org.sopt.confeti.api.user.dto.request.AddTimetableRequest;
import org.sopt.confeti.api.user.dto.request.PatchTimetableFestivalRequest;
import org.sopt.confeti.api.user.dto.request.PatchTimetableRequest;
import org.sopt.confeti.api.user.dto.response.TimetableDatesResponse;
import org.sopt.confeti.api.user.dto.response.TimetablesToAddResponse;
import org.sopt.confeti.api.user.dto.response.TimetableCursorResponse;
import org.sopt.confeti.api.user.dto.response.TimetableDetailFestivalsResponse;
import org.sopt.confeti.api.user.dto.response.TimetableEntireFestivalResponse;
import org.sopt.confeti.api.user.dto.response.TimetableFestivalResponse;
import org.sopt.confeti.api.user.dto.response.TimetableHistoryResponse;
import org.sopt.confeti.api.user.dto.response.TimetablesPreviewResponse;
import org.sopt.confeti.api.user.dto.response.TimetablesResponse;
import org.sopt.confeti.api.user.facade.UserTimetableFacade;
import org.sopt.confeti.api.user.facade.dto.request.AddTimetableDTO;
import org.sopt.confeti.api.user.facade.dto.request.PatchTimeBlockDTO;
import org.sopt.confeti.api.user.facade.dto.response.TimetableDatesDTO;
import org.sopt.confeti.api.user.facade.dto.response.TimetableToAddDTO;
import org.sopt.confeti.api.user.facade.dto.response.TimetableDetailFestivalsDTO;
import org.sopt.confeti.api.user.facade.dto.response.TimetableEntireFestivalDTO;
import org.sopt.confeti.api.user.facade.dto.response.TimetableFestivalBasicDTO;
import org.sopt.confeti.api.user.facade.dto.response.TimetableHistoryDTO;
import org.sopt.confeti.api.user.facade.dto.response.TimetablesDTO;
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
import org.sopt.confeti.global.interceptor.auth.UserContext;
import org.sopt.confeti.global.message.SuccessMessage;
import org.sopt.confeti.global.util.ApiResponseUtil;
import org.sopt.confeti.global.util.S3FileHandler;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
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

    private final UserTimetableFacade timetableFacade;
    private final S3FileHandler s3FileHandler;

    @Permission(role = {Role.GENERAL})
    @GetMapping("/festivals")
    public ResponseEntity<BaseResponse<TimetableDetailFestivalsResponse>> getTimetablesListAndDate() {
        TimetableDetailFestivalsDTO timetableDetailFestivalsDTO = timetableFacade.getTimetablesListAndDate();
        return ApiResponseUtil.success(SuccessMessage.SUCCESS,
            TimetableDetailFestivalsResponse.of(timetableDetailFestivalsDTO,
                s3FileHandler));
    }

    @Permission(role = {Role.GENERAL})
    @GetMapping("/festivals/history")
    public ResponseEntity<BaseResponse<TimetableHistoryResponse>> getHasTimetableHistory() {
        TimetableHistoryDTO timetableHistoryDTO = timetableFacade.getHasTimetableHistory();
        return ApiResponseUtil.success(SuccessMessage.SUCCESS,
            TimetableHistoryResponse.from(timetableHistoryDTO));
    }

    @Permission(role = {Role.GENERAL})
    @GetMapping("/festivals/add")
    public ResponseEntity<BaseResponse<TimetablesToAddResponse>> getTimetablesToAdd(
        @RequestParam(name = "cursor", required = false) Long cursor
    ) {
        CursorPage<TimetableToAddDTO> timetablesToAdd = timetableFacade.getTimetablesToAdd(
            cursor);
        return ApiResponseUtil.success(SuccessMessage.SUCCESS,
            TimetablesToAddResponse.of(timetablesToAdd, s3FileHandler));
    }

    @Permission(role = {Role.GENERAL})
    @PostMapping("/festivals")
    public ResponseEntity<BaseResponse<Void>> addTimetableFestival(
        @RequestBody AddTimetableRequest addTimetableRequest
    ) {
        timetableFacade.addTimetables(
            AddTimetableDTO.from(addTimetableRequest));
        return ApiResponseUtil.success(SuccessMessage.SUCCESS);
    }

    @Permission(role = {Role.GENERAL})
    @DeleteMapping("/festivals/{festivalId}")
    public ResponseEntity<BaseResponse<Void>> removeTimetableFestival(
        @PathVariable(name = "festivalId") @Min(RequestConstraint.ID) long festivalId
    ) {
        timetableFacade.removeTimetable(festivalId);
        return ApiResponseUtil.success(SuccessMessage.SUCCESS);
    }

    @Permission(role = {Role.GENERAL})
    @GetMapping("/festivals/{festivalDateId}")
    public ResponseEntity<BaseResponse<TimetableFestivalResponse>> getTimetableFestival(
        @PathVariable(name = "festivalDateId") @Min(RequestConstraint.ID) long festivalDateId
    ) {
        TimetableFestivalBasicDTO response = timetableFacade.getTimetableInfo(
            festivalDateId);
        return ApiResponseUtil.success(SuccessMessage.SUCCESS,
            TimetableFestivalResponse.from(response));
    }

    @Permission(role = {Role.GENERAL})
    @PatchMapping("/festivals")
    public ResponseEntity<BaseResponse<Void>> updateTimetableFestival(
        @RequestBody PatchTimetableRequest patchTimetableRequest
    ) {
        timetableFacade.patchTimeBlocks(PatchTimeBlockDTO.from(patchTimetableRequest));
        return ApiResponseUtil.success(SuccessMessage.SUCCESS);
    }

    @GetMapping
    @Deprecated
    @Permission(role = {Role.GENERAL})
    public ResponseEntity<BaseResponse<TimetablesResponse>> getTimetables(
        @RequestParam(value = "sortBy", defaultValue = "createdAt") String sortBy
    ) {
        TimetablesDTO timetables = timetableFacade.getTimetables_deprecated(
            UserContext.get().id(), sortBy);
        return ApiResponseUtil.success(SuccessMessage.SUCCESS,
            TimetablesResponse.of(timetables, s3FileHandler));
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
        CursorPage<Timetable> timetableCursorPage = timetableFacade.getTimetableCursorPage(
            sortBy, cursorData, status);
        return ApiResponseUtil.success(SuccessMessage.SUCCESS,
            TimetableCursorResponse.of(timetableCursorPage, s3FileHandler));
    }

    @Permission(role = {Role.GENERAL})
    @GetMapping("/preview")
    public ResponseEntity<BaseResponse<TimetablesPreviewResponse>> getTimetablesPreview() {
        TimetablesDTO timetables = timetableFacade.getTimetablesPreview();
        return ApiResponseUtil.success(SuccessMessage.SUCCESS,
            TimetablesPreviewResponse.of(timetables, s3FileHandler));
    }

    @Permission(role = {Role.GENERAL})
    @GetMapping("/archive/{festivalId}")
    public ResponseEntity<BaseResponse<TimetableEntireFestivalResponse>> getEntireFestivalInfo(
        @PathVariable(name = "festivalId") @Min(RequestConstraint.ID) Long festivalId
    ) {
        TimetableEntireFestivalDTO entireFestivalDTO = timetableFacade.getEntireFestivalInfo(
            festivalId);
        return ApiResponseUtil.success(SuccessMessage.SUCCESS,
            TimetableEntireFestivalResponse.of(entireFestivalDTO, s3FileHandler));
    }

    @Permission(role = {Role.GENERAL})
    @GetMapping("/archive/festival/{festivalDateId}")
    public ResponseEntity<BaseResponse<TimetableFestivalResponse>> getEntireFestivalDateInfo(
        @PathVariable(name = "festivalDateId") @Min(RequestConstraint.ID) Long festivalDateId
    ) {
        TimetableFestivalBasicDTO festivalBasicDTO = timetableFacade.getEntireFestivalDateInfo(
            festivalDateId);
        return ApiResponseUtil.success(SuccessMessage.SUCCESS,
            TimetableFestivalResponse.from(festivalBasicDTO));
    }

    @ApiVersion("2")
    @Permission(role = {Role.GENERAL})
    @PatchMapping("/festivals")
    public ResponseEntity<BaseResponse<Void>> updateTimetableFestival(
        @RequestBody PatchTimetableFestivalRequest patchTimetableFestivalRequest
    ) {
        timetableFacade.updateTimetables(patchTimetableFestivalRequest.toDTO());
        return ApiResponseUtil.success(SuccessMessage.SUCCESS);
    }

    @Permission(role = {Role.GENERAL})
    @GetMapping("/{timetableId}/dates")
    public ResponseEntity<BaseResponse<TimetableDatesResponse>> getTimetableDates(
        @PathVariable Long timetableId
    ) {
        TimetableDatesDTO timetableDates = timetableFacade.getTimetableDates(
            timetableId);
        return ApiResponseUtil.success(SuccessMessage.SUCCESS,
            TimetableDatesResponse.of(timetableDates, s3FileHandler));
    }
}
