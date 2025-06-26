package org.sopt.confeti.api.user.controller;

import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.sopt.confeti.api.user.dto.request.AddTimetableFestivalRequest;
import org.sopt.confeti.api.user.dto.request.PatchTimetableRequest;
import org.sopt.confeti.api.user.dto.response.TimetablesToAddResponse;
import org.sopt.confeti.api.user.dto.response.UserTimetableDetailFestivalsResponse;
import org.sopt.confeti.api.user.dto.response.UserTimetableFestivalResponse;
import org.sopt.confeti.api.user.dto.response.UserTimetableHistoryResponse;
import org.sopt.confeti.api.user.dto.response.UserTimetablesPreviewResponse;
import org.sopt.confeti.api.user.dto.response.UserTimetablesResponse;
import org.sopt.confeti.api.user.dto.response.UserTimetablePastFestivalResponse;
import org.sopt.confeti.api.user.facade.UserTimetableFacade;
import org.sopt.confeti.api.user.facade.dto.request.AddTimetableFestivalDTO;
import org.sopt.confeti.api.user.facade.dto.request.PatchTimetableDTO;
import org.sopt.confeti.api.user.facade.dto.response.TimetableToAddDTO;
import org.sopt.confeti.api.user.facade.dto.response.UserTimetableDetailFestivalsDTO;
import org.sopt.confeti.api.user.facade.dto.response.UserTimetableFestivalBasicDTO;
import org.sopt.confeti.api.user.facade.dto.response.UserTimetableHistoryDTO;
import org.sopt.confeti.api.user.facade.dto.response.UserTimetablesDTO;
import org.sopt.confeti.api.user.facade.dto.response.UserTimetablePastFestivalDTO;
import org.sopt.confeti.domain.user.constant.Role;
import org.sopt.confeti.global.annotation.Permission;
import org.sopt.confeti.global.annotation.UserId;
import org.sopt.confeti.global.common.BaseResponse;
import org.sopt.confeti.global.common.CursorPage;
import org.sopt.confeti.global.common.constant.RequestConstraint;
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
public class UserTimetableController {

    private final UserTimetableFacade userTimetableFacade;
    private final S3FileHandler s3FileHandler;

    @Permission(role = {Role.GENERAL})
    @GetMapping("/festivals")
    public ResponseEntity<BaseResponse<?>> getTimetablesListAndDate(
            @UserId Long userId
    ) {
        UserTimetableDetailFestivalsDTO userTimetableDetailFestivalsDTO = userTimetableFacade.getTimetablesListAndDate(
                userId);
        return ApiResponseUtil.success(SuccessMessage.SUCCESS,
                UserTimetableDetailFestivalsResponse.of(userTimetableDetailFestivalsDTO, s3FileHandler));
    }

    @Permission(role = {Role.GENERAL})
    @GetMapping("/festivals/history")
    public ResponseEntity<BaseResponse<?>> getHasTimetableHistory(
            @UserId Long userId
    ) {
        UserTimetableHistoryDTO timetableHistoryDTO = userTimetableFacade.getHasTimetableHistory(userId);
        return ApiResponseUtil.success(SuccessMessage.SUCCESS, UserTimetableHistoryResponse.from(timetableHistoryDTO));
    }

    @Permission(role = {Role.GENERAL})
    @GetMapping("/festivals/add")
    public ResponseEntity<BaseResponse<?>> getTimetablesToAdd(
            @UserId Long userId,
            @RequestParam(name = "cursor", required = false) Long cursor
    ) {
        CursorPage<TimetableToAddDTO> timetablesToAdd = userTimetableFacade.getTimetablesToAdd(userId, cursor);
        return ApiResponseUtil.success(SuccessMessage.SUCCESS,
                TimetablesToAddResponse.of(timetablesToAdd, s3FileHandler));
    }

    @Permission(role = {Role.GENERAL})
    @PostMapping("/festivals")
    public ResponseEntity<BaseResponse<?>> addTimetableFestival(
            @UserId Long userId,
            @RequestBody AddTimetableFestivalRequest addTimetableFestivalRequest
    ) {
        userTimetableFacade.addTimetableFestivals(userId, AddTimetableFestivalDTO.from(addTimetableFestivalRequest));
        return ApiResponseUtil.success(SuccessMessage.SUCCESS);
    }

    @Permission(role = {Role.GENERAL})
    @DeleteMapping("/festivals/{festivalId}")
    public ResponseEntity<BaseResponse<?>> removeTimetableFestival(
            @UserId Long userId,
            @PathVariable(name = "festivalId") @Min(RequestConstraint.ID) long festivalId
    ) {
        userTimetableFacade.removeTimetableFestival(userId, festivalId);
        return ApiResponseUtil.success(SuccessMessage.SUCCESS);
    }

    @Permission(role = {Role.GENERAL})
    @GetMapping("/festivals/{festivalDateId}")
    public ResponseEntity<BaseResponse<?>> getTimetableFestival(
            @UserId Long userId,
            @PathVariable(name = "festivalDateId") @Min(RequestConstraint.ID) long festivalDateId
    ) {
        UserTimetableFestivalBasicDTO response = userTimetableFacade.getTimetableInfo(userId, festivalDateId);
        return ApiResponseUtil.success(SuccessMessage.SUCCESS, UserTimetableFestivalResponse.from(response));
    }

    @Permission(role = {Role.GENERAL})
    @PatchMapping("/festivals")
    public ResponseEntity<BaseResponse<?>> updateTimetableFestival(
            @UserId Long userId,
            @RequestBody PatchTimetableRequest patchTimetablerequest
    ) {
        userTimetableFacade.patchTimetableFestivals(userId, PatchTimetableDTO.from(patchTimetablerequest));
        return ApiResponseUtil.success(SuccessMessage.SUCCESS);
    }

    @GetMapping
    public ResponseEntity<BaseResponse<?>> getTimetables(
            @UserId Long userId,
            @RequestParam(value = "sortBy", defaultValue = "createdAt") String sortBy
    ) {
        UserTimetablesDTO timetables = userTimetableFacade.getTimetables(userId, sortBy);
        return ApiResponseUtil.success(SuccessMessage.SUCCESS, UserTimetablesResponse.of(timetables, s3FileHandler));
    }

    @GetMapping("/preview")
    public ResponseEntity<BaseResponse<?>> getTimetablesPreview(
            @UserId Long userId
    ) {
        UserTimetablesDTO timetables = userTimetableFacade.getTimetablesPreview(userId);
        return ApiResponseUtil.success(SuccessMessage.SUCCESS,
                UserTimetablesPreviewResponse.of(timetables, s3FileHandler));
    }

    @GetMapping("/history/{festivalId}")
    public ResponseEntity<BaseResponse<?>> getPastFestivalInfo(
            @UserId Long userId,
            @PathVariable(name = "festivalId") @Min(RequestConstraint.ID) Long festivalId
    ) {
        UserTimetablePastFestivalDTO pastFestivalDTO = userTimetableFacade.getPastFestivalInfo(userId, festivalId);
        return ApiResponseUtil.success(SuccessMessage.SUCCESS,
                UserTimetablePastFestivalResponse.of(pastFestivalDTO, s3FileHandler));
    }
}
