package org.sopt.confeti.api.user.controller;

import lombok.RequiredArgsConstructor;
import org.sopt.confeti.api.user.dto.response.UserTimetableDetailResponse;
import org.sopt.confeti.api.user.facade.UserTimetableFacade;
import org.sopt.confeti.api.user.facade.dto.response.UserTimetableDTO;
import org.sopt.confeti.global.common.BaseResponse;
import org.sopt.confeti.global.message.SuccessMessage;
import org.sopt.confeti.global.util.ApiResponseUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user/timetables/festivals")
public class UserTimetableController {
    private final UserTimetableFacade userTimetableFacade;

    @GetMapping
    public ResponseEntity<BaseResponse<?>> getTimetablesListAndDate(@RequestHeader("Authorization") long userId) {
        UserTimetableDTO usereTimetableDTO =  userTimetableFacade.getTimetablesListAndDate(userId);
        return ApiResponseUtil.success(SuccessMessage.SUCCESS, UserTimetableDetailResponse.from(usereTimetableDTO));
    }
}
