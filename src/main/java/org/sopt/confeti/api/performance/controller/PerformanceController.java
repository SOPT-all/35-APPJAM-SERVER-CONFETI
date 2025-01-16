package org.sopt.confeti.api.performance.controller;

import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.sopt.confeti.api.performance.dto.response.ConcertDetailResponse;
import org.sopt.confeti.api.performance.facade.PerformanceFacade;
import org.sopt.confeti.api.performance.facade.dto.response.ConcertDetailDTO;
import org.sopt.confeti.global.common.BaseResponse;
import org.sopt.confeti.global.message.SuccessMessage;
import org.sopt.confeti.global.util.ApiResponseUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping("/performances")
public class PerformanceController {

    private final PerformanceFacade performanceFacade;

    @GetMapping("/concerts/{concertId}")
    public ResponseEntity<BaseResponse<?>> getConcertInfo(@RequestHeader("Authorization") Long userId,
                                                          @PathVariable @Min(value = 0, message = "요청 형식이 올바르지 않습니다.") Long concertId) {
        ConcertDetailDTO concertDetailDTO = performanceFacade.getConcertDetailInfo(concertId);
        return ApiResponseUtil.success(SuccessMessage.SUCCESS, ConcertDetailResponse.from(concertDetailDTO));
    }
}
