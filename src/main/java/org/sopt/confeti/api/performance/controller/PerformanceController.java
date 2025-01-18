package org.sopt.confeti.api.performance.controller;

import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.sopt.confeti.api.performance.dto.request.CreateFestivalRequest;
import org.sopt.confeti.api.performance.dto.response.ConcertDetailResponse;
import org.sopt.confeti.api.performance.dto.response.FestivalDetailResponse;
import org.sopt.confeti.api.performance.facade.PerformanceFacade;
import org.sopt.confeti.api.performance.facade.dto.request.CreateFestivalDTO;
import org.sopt.confeti.api.performance.facade.dto.response.ConcertDetailDTO;
import org.sopt.confeti.api.performance.facade.dto.response.FestivalDetailDTO;
import org.sopt.confeti.global.common.BaseResponse;
import org.sopt.confeti.global.message.SuccessMessage;
import org.sopt.confeti.global.util.ApiResponseUtil;
import org.sopt.confeti.global.util.S3FileHandler;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping("/performances")
public class PerformanceController {

    private final PerformanceFacade performanceFacade;
    private final S3FileHandler s3FileHandler;

    @GetMapping("/concerts/{concertId}")
    public ResponseEntity<BaseResponse<?>> getConcertInfo(
            @RequestHeader(name = "Authorization", required = false) Long userId,
            @PathVariable("concertId") @Min(value = 0, message = "요청 형식이 올바르지 않습니다.") Long concertId
    ) {
        ConcertDetailDTO concertDetailDTO = performanceFacade.getConcertDetailInfo(concertId);
        return ApiResponseUtil.success(SuccessMessage.SUCCESS, ConcertDetailResponse.of(concertDetailDTO, s3FileHandler));
    }

    @GetMapping("/festivals/{festivalId}")
    public ResponseEntity<BaseResponse<?>> getFestivalInfo(@RequestHeader(name = "Authorization", required = false) Long userId,
                                                           @PathVariable("festivalId") @Min(value = 0, message = "요청 형식이 올바르지 않습니다.") Long festivalId) {
        FestivalDetailDTO festivalDetailDTO = performanceFacade.getFestivalDetailInfo(userId, festivalId);
        return ApiResponseUtil.success(SuccessMessage.SUCCESS, FestivalDetailResponse.from(festivalDetailDTO));
    }

    @GetMapping("/festivals/{festivalId}")
    public ResponseEntity<BaseResponse<?>> getFestivalInfo(@RequestHeader(name = "Authorization", required = false) Long userId,
                                                           @PathVariable("festivalId") @Min(value = 0, message = "요청 형식이 올바르지 않습니다.") Long festivalId) {
        FestivalDetailDTO festivalDetailDTO = performanceFacade.getFestivalDetailInfo(userId, festivalId);
        return ApiResponseUtil.success(SuccessMessage.SUCCESS, FestivalDetailResponse.from(festivalDetailDTO));
    }

    @PostMapping("/festivals")
    public ResponseEntity<BaseResponse<?>> createConcert(@RequestBody CreateFestivalRequest createFestivalRequest) {
        performanceFacade.createFestival(CreateFestivalDTO.from(createFestivalRequest));

        return ApiResponseUtil.success(SuccessMessage.SUCCESS);
    }
}
