package org.sopt.confeti.api.user.controller;

import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.sopt.confeti.api.user.dto.request.AddTimetableFestivalRequest;
import org.sopt.confeti.api.user.dto.request.PatchTimetableRequest;
import org.sopt.confeti.api.user.dto.response.TimetablesToAddResponse;
import org.sopt.confeti.api.user.dto.response.UserTimetableDetailResponse;
import org.sopt.confeti.api.user.dto.response.UserTimetableFestivalResponse;
import org.sopt.confeti.api.user.facade.UserTimetableFacade;
import org.sopt.confeti.api.user.facade.dto.request.AddTimetableFestivalDTO;
import org.sopt.confeti.api.user.facade.dto.request.PatchTimetableDTO;
import org.sopt.confeti.api.user.facade.dto.response.TimetableToAddDTO;
import org.sopt.confeti.api.user.facade.dto.response.UserTimetableDTO;
import org.sopt.confeti.api.user.facade.dto.response.UserTimetableFestivalBasicDTO;
import org.sopt.confeti.api.user.facade.dto.response.UserTimetableFestivalDTO;
import org.sopt.confeti.global.common.BaseResponse;
import org.sopt.confeti.global.common.CursorPage;
import org.sopt.confeti.global.message.SuccessMessage;
import org.sopt.confeti.global.util.ApiResponseUtil;
import org.sopt.confeti.global.util.S3FileHandler;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@Validated
@RequiredArgsConstructor
@RequestMapping("/user/timetables/festivals")
public class UserTimetableController {

    private final UserTimetableFacade userTimetableFacade;
    private final S3FileHandler s3FileHandler;

    @GetMapping
    public ResponseEntity<BaseResponse<?>> getTimetablesListAndDate(@RequestHeader("Authorization") long userId) {
        UserTimetableDTO userTimetableDTO =  userTimetableFacade.getTimetablesListAndDate(userId);
        return ApiResponseUtil.success(SuccessMessage.SUCCESS, UserTimetableDetailResponse.of(userTimetableDTO, s3FileHandler));
    }

    @GetMapping("/add")
    public ResponseEntity<BaseResponse<?>> getTimetablesToAdd(
            @RequestHeader("Authorization") long userId,
            @RequestParam(name = "cursor", required = false) Long cursor
    ) {
        CursorPage<TimetableToAddDTO> timetablesToAdd = userTimetableFacade.getTimetablesToAdd(userId, cursor);
        return ApiResponseUtil.success(SuccessMessage.SUCCESS, TimetablesToAddResponse.of(timetablesToAdd, s3FileHandler));
    }

    @PostMapping
    public ResponseEntity<BaseResponse<?>> addTimetableFestival(
            @RequestHeader("Authorization") long userId,
            @RequestBody AddTimetableFestivalRequest addTimetableFestivalRequest
    ) {
        userTimetableFacade.addTimetableFestivals(userId, AddTimetableFestivalDTO.from(addTimetableFestivalRequest));
        return ApiResponseUtil.success(SuccessMessage.SUCCESS);
    }

    @DeleteMapping("/{festivalId}")
    public ResponseEntity<BaseResponse<?>> removeTimetableFestival(
            @RequestHeader("Authorization") long userId,
            @PathVariable(name = "festivalId") @Min(value = 0, message = "요청 형식이 올바르지 않습니다.") long festivalId
    ) {
        userTimetableFacade.removeTimetableFestival(userId, festivalId);
        return ApiResponseUtil.success(SuccessMessage.SUCCESS);
    }

    @GetMapping("/{festivalDateId}")
    public ResponseEntity<BaseResponse<?>> getTimetableFestival(
            @RequestHeader("Authorization") long userId,
            @PathVariable(name = "festivalDateId") @Min(value=0, message="요청 형식이 올바르지 않습니다.") long festivalDateId
    ){
        UserTimetableFestivalBasicDTO response = userTimetableFacade.getTimetableInfo(userId, festivalDateId);
        return ApiResponseUtil.success(SuccessMessage.SUCCESS, UserTimetableFestivalResponse.from(response));
    }

    @PatchMapping
    public ResponseEntity<BaseResponse<?>> updateTimetableFestival(
            @RequestHeader("Authorization") long userId,
            @RequestBody PatchTimetableRequest patchTimetablerequest
    ){
        userTimetableFacade.patchTimetableFestivals(userId, PatchTimetableDTO.from(patchTimetablerequest));
        return ApiResponseUtil.success(SuccessMessage.SUCCESS);
    }
}
