package org.sopt.confeti.api.user.controller;

import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.sopt.confeti.api.user.dto.request.AddTimetableFestivalRequest;
import org.sopt.confeti.api.user.dto.response.UserTimetableDetailResponse;
import org.sopt.confeti.api.user.facade.UserTimetableFacade;
import org.sopt.confeti.api.user.facade.dto.request.AddTimetableFestivalDTO;
import org.sopt.confeti.api.user.facade.dto.response.UserTimetableDTO;
import org.sopt.confeti.global.common.BaseResponse;
import org.sopt.confeti.global.message.SuccessMessage;
import org.sopt.confeti.global.util.ApiResponseUtil;
import org.sopt.confeti.global.util.S3FileHandler;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
