package org.sopt.confeti.api.controller;

import lombok.RequiredArgsConstructor;
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
public class HomeViewController {

    @GetMapping("/performances/reservation")
    public ResponseEntity<BaseResponse<?>> performanceReservation(@RequestHeader("Authorization") String userId ) {
        String a = "Aa"; //임시값
        return ApiResponseUtil.success(SuccessMessage.SUCCESS, a); //임시값
    }

}
