package org.sopt.confeti.api.user.controller;

import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.sopt.confeti.api.user.controller.docs.UserTimetableControllerV4Docs;
import org.sopt.confeti.api.user.dto.request.AddTimetableFestivalRequest;
import org.sopt.confeti.api.user.dto.request.PatchTimetableFestivalRequest;
import org.sopt.confeti.api.user.dto.request.PatchTimetableRequest;
import org.sopt.confeti.api.user.dto.response.TimetableDatesResponse;
import org.sopt.confeti.api.user.dto.response.TimetablesToAddResponse;
import org.sopt.confeti.api.user.dto.response.UserTimetableCursorResponse;
import org.sopt.confeti.api.user.dto.response.UserTimetableDetailFestivalsResponse;
import org.sopt.confeti.api.user.dto.response.UserTimetableEntireFestivalResponse;
import org.sopt.confeti.api.user.dto.response.UserTimetableFestivalResponse;
import org.sopt.confeti.api.user.dto.response.UserTimetableHistoryResponse;
import org.sopt.confeti.api.user.dto.response.UserTimetablesPreviewResponse;
import org.sopt.confeti.api.user.facade.UserTimetableFacade;
import org.sopt.confeti.api.user.facade.dto.request.AddTimetableFestivalDTO;
import org.sopt.confeti.api.user.facade.dto.request.PatchTimetableDTO;
import org.sopt.confeti.api.user.facade.dto.response.TimetableDatesDTO;
import org.sopt.confeti.api.user.facade.dto.response.TimetableToAddDTO;
import org.sopt.confeti.api.user.facade.dto.response.UserTimetableDetailFestivalsDTO;
import org.sopt.confeti.api.user.facade.dto.response.UserTimetableEntireFestivalDTO;
import org.sopt.confeti.api.user.facade.dto.response.UserTimetableFestivalBasicDTO;
import org.sopt.confeti.api.user.facade.dto.response.UserTimetableHistoryDTO;
import org.sopt.confeti.api.user.facade.dto.response.UserTimetablesDTO;
import org.sopt.confeti.domain.timetable_festival.TimetableFestival;
import org.sopt.confeti.domain.timetable_festival.TimetableFestivalCursor;
import org.sopt.confeti.domain.timetable_festival.TimetableFestivalCursor.CursorData;
import org.sopt.confeti.domain.user.constant.Role;
import org.sopt.confeti.global.annotation.Permission;
import org.sopt.confeti.global.annotation.UserId;
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
@RequestMapping("/user/timetables/v4")
public class UserTimetableControllerV4 implements UserTimetableControllerV4Docs {

    private final UserTimetableFacade userTimetableFacade;
    private final S3FileHandler s3FileHandler;

    @Permission(role = {Role.GENERAL})
    @GetMapping("/festivals")
    public ResponseEntity<BaseResponse<UserTimetableDetailFestivalsResponse>> getTimetablesListAndDate(
        @UserId Long userId
    ) {
        UserTimetableDetailFestivalsDTO userTimetableDetailFestivalsDTO = userTimetableFacade.getTimetablesListAndDate(
            userId);
        return ApiResponseUtil.success(SuccessMessage.SUCCESS,
            UserTimetableDetailFestivalsResponse.of(userTimetableDetailFestivalsDTO,
                s3FileHandler));
    }

    @Permission(role = {Role.GENERAL})
    @GetMapping("/festivals/history")
    public ResponseEntity<BaseResponse<UserTimetableHistoryResponse>> getHasTimetableHistory(
        @UserId Long userId
    ) {
        UserTimetableHistoryDTO timetableHistoryDTO = userTimetableFacade.getHasTimetableHistory(
            userId);
        return ApiResponseUtil.success(SuccessMessage.SUCCESS,
            UserTimetableHistoryResponse.from(timetableHistoryDTO));
    }

    @Permission(role = {Role.GENERAL})
    @GetMapping("/festivals/add")
    public ResponseEntity<BaseResponse<TimetablesToAddResponse>> getTimetablesToAdd(
        @UserId Long userId,
        @RequestParam(name = "cursor", required = false) Long cursor
    ) {
        CursorPage<TimetableToAddDTO> timetablesToAdd = userTimetableFacade.getTimetablesToAdd(
            userId, cursor);
        return ApiResponseUtil.success(SuccessMessage.SUCCESS,
            TimetablesToAddResponse.of(timetablesToAdd, s3FileHandler));
    }

    @Permission(role = {Role.GENERAL})
    @PostMapping("/festivals")
    public ResponseEntity<BaseResponse<Void>> addTimetableFestival(
        @UserId Long userId,
        @RequestBody AddTimetableFestivalRequest addTimetableFestivalRequest
    ) {
        userTimetableFacade.addTimetableFestivals(userId,
            AddTimetableFestivalDTO.from(addTimetableFestivalRequest));
        return ApiResponseUtil.success(SuccessMessage.SUCCESS);
    }

    @Permission(role = {Role.GENERAL})
    @DeleteMapping("/festivals/{festivalId}")
    public ResponseEntity<BaseResponse<Void>> removeTimetableFestival(
        @UserId Long userId,
        @PathVariable(name = "festivalId") @Min(RequestConstraint.ID) long festivalId
    ) {
        userTimetableFacade.removeTimetableFestival(userId, festivalId);
        return ApiResponseUtil.success(SuccessMessage.SUCCESS);
    }

    @Permission(role = {Role.GENERAL})
    @GetMapping("/festivals/{festivalDateId}")
    public ResponseEntity<BaseResponse<UserTimetableFestivalResponse>> getTimetableFestival(
        @UserId Long userId,
        @PathVariable(name = "festivalDateId") @Min(RequestConstraint.ID) long festivalDateId
    ) {
        UserTimetableFestivalBasicDTO response = userTimetableFacade.getTimetableInfo(userId,
            festivalDateId);
        return ApiResponseUtil.success(SuccessMessage.SUCCESS,
            UserTimetableFestivalResponse.from(response));
    }

    @Permission(role = {Role.GENERAL})
    @PatchMapping
    public ResponseEntity<BaseResponse<Void>> updateTimetable(
        @UserId Long userId,
        @RequestBody PatchTimetableRequest patchTimetablerequest
    ) {
        userTimetableFacade.patchTimetableFestivals(userId,
            PatchTimetableDTO.from(patchTimetablerequest));
        return ApiResponseUtil.success(SuccessMessage.SUCCESS);
    }

    @GetMapping
    public ResponseEntity<BaseResponse<UserTimetableCursorResponse>> getTimetables(
        @UserId Long userId,
        @RequestParam(defaultValue = Default.TIMETABLE_SORT_TYPE) TimetableSortType sortBy,
        @RequestParam(required = false) String cursor,
        @RequestParam(defaultValue = Default.PERFORMANCE_STATUS) PerformanceStatus status
    ) {
        CursorData cursorData = TimetableFestivalCursor.decode(cursor);
        CursorPage<TimetableFestival> timetableCursorPage = userTimetableFacade.getTimetableCursorPage(
            userId, sortBy, cursorData, status);
        return ApiResponseUtil.success(SuccessMessage.SUCCESS,
            UserTimetableCursorResponse.of(timetableCursorPage, s3FileHandler));
    }

    @GetMapping("/preview")
    public ResponseEntity<BaseResponse<UserTimetablesPreviewResponse>> getTimetablesPreview(
        @UserId Long userId
    ) {
        UserTimetablesDTO timetables = userTimetableFacade.getTimetablesPreview(userId);
        return ApiResponseUtil.success(SuccessMessage.SUCCESS,
            UserTimetablesPreviewResponse.of(timetables, s3FileHandler));
    }

    @GetMapping("/archive/{festivalId}")
    public ResponseEntity<BaseResponse<UserTimetableEntireFestivalResponse>> getEntireFestivalInfo(
        @UserId Long userId,
        @PathVariable(name = "festivalId") @Min(RequestConstraint.ID) Long festivalId
    ) {
        UserTimetableEntireFestivalDTO entireFestivalDTO = userTimetableFacade.getEntireFestivalInfo(
            userId, festivalId);
        return ApiResponseUtil.success(SuccessMessage.SUCCESS,
            UserTimetableEntireFestivalResponse.of(entireFestivalDTO, s3FileHandler));
    }

    @GetMapping("/archive/festival/{festivalDateId}")
    public ResponseEntity<BaseResponse<UserTimetableFestivalResponse>> getEntireFestivalDateInfo(
        @UserId Long userId,
        @PathVariable(name = "festivalDateId") @Min(RequestConstraint.ID) Long festivalDateId
    ) {
        UserTimetableFestivalBasicDTO festivalBasicDTO = userTimetableFacade.getEntireFestivalDateInfo(
            userId, festivalDateId);
        return ApiResponseUtil.success(SuccessMessage.SUCCESS,
            UserTimetableFestivalResponse.from(festivalBasicDTO));
    }

    @Permission(role = {Role.GENERAL})
    @PatchMapping("/festivals")
    public ResponseEntity<BaseResponse<Void>> updateTimetableFestival(
        @UserId Long userId,
        @RequestBody PatchTimetableFestivalRequest patchTimetableFestivalRequest
    ) {
        userTimetableFacade.updateTimetableFestivals(userId,
            patchTimetableFestivalRequest.toDTO());
        return ApiResponseUtil.success(SuccessMessage.SUCCESS);
    }

    @Permission(role = {Role.GENERAL})
    @GetMapping("/{timetableFestivalId}/dates")
    public ResponseEntity<BaseResponse<TimetableDatesResponse>> getTimetableDates(
        @UserId Long userId,
        @PathVariable Long timetableFestivalId
    ) {
        TimetableDatesDTO timetableDates = userTimetableFacade.getTimetableDates(userId,
            timetableFestivalId);
        return ApiResponseUtil.success(SuccessMessage.SUCCESS,
            TimetableDatesResponse.of(timetableDates, s3FileHandler));
    }
}
