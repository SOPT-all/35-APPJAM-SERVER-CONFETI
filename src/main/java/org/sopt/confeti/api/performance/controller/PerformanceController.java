package org.sopt.confeti.api.performance.controller;

import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.sopt.confeti.api.performance.dto.request.CreateConcertRequest;
import org.sopt.confeti.api.performance.dto.request.CreateFestivalRequest;
import org.sopt.confeti.api.performance.dto.response.*;
import org.sopt.confeti.api.performance.facade.PerformanceFacade;
import org.sopt.confeti.api.performance.facade.dto.request.CreateConcertDTO;
import org.sopt.confeti.api.performance.facade.dto.request.CreateFestivalDTO;
import org.sopt.confeti.api.performance.facade.dto.response.*;
import org.sopt.confeti.api.performance.facade.dto.response.PerformanceReservationDTO;
import org.sopt.confeti.api.performance.facade.dto.response.RecentPerformancesDTO;
import org.sopt.confeti.global.common.BaseResponse;
import org.sopt.confeti.global.message.SuccessMessage;
import org.sopt.confeti.global.util.ApiResponseUtil;
import org.sopt.confeti.global.util.S3FileHandler;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

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
            @PathVariable("concertId") @Min(value = 0, message = "요청 형식이 올바르지 않습니다.") long concertId
    ) {
        ConcertDetailDTO concertDetailDTO = performanceFacade.getConcertDetailInfo(userId, concertId);
        return ApiResponseUtil.success(SuccessMessage.SUCCESS, ConcertDetailResponse.of(concertDetailDTO, s3FileHandler));
    }

    @GetMapping("/festivals/{festivalId}")
    public ResponseEntity<BaseResponse<?>> getFestivalInfo(@RequestHeader(name = "Authorization", required = false) Long userId,
                                                           @PathVariable("festivalId") @Min(value = 0, message = "요청 형식이 올바르지 않습니다.") Long festivalId) {
        FestivalDetailDTO festivalDetailDTO = performanceFacade.getFestivalDetailInfo(userId, festivalId);
        return ApiResponseUtil.success(SuccessMessage.SUCCESS, FestivalDetailResponse.from(festivalDetailDTO));
    }

    @PostMapping("/concerts")
    public ResponseEntity<BaseResponse<?>> createConcert(@RequestBody CreateConcertRequest createConcertRequest) {
        performanceFacade.createConcert(CreateConcertDTO.from(createConcertRequest));
        return ApiResponseUtil.success(SuccessMessage.CREATED);
    }

    @PostMapping("/festivals")
    public ResponseEntity<BaseResponse<?>> createFestival(@RequestBody CreateFestivalRequest createFestivalRequest) {
        performanceFacade.createFestival(CreateFestivalDTO.from(createFestivalRequest));

        return ApiResponseUtil.success(SuccessMessage.CREATED);
    }

    @GetMapping("/reservation")
    public ResponseEntity<BaseResponse<?>> getPerformReservationInfo(@RequestHeader(name = "Authorization", required = false) Long userId
    ) {
        PerformanceReservationDTO performanceReservationDTO = performanceFacade.getPerformReservationInfo(userId);
        return ApiResponseUtil.success(SuccessMessage.SUCCESS, PerformanceReservationResponse.of(performanceReservationDTO, s3FileHandler));
    }

    @GetMapping("/association/{artistId}")
    public ResponseEntity<BaseResponse<?>> getPerformanceByArtist(
            @RequestHeader(name="Authorization", required = false) Long userId,
            @PathVariable(name="artistId") String artistId,
            @RequestParam(name="cursor", required = false) Long cursor
    ){
        PerformanceByArtistDTO performances = performanceFacade.getPerformanceByArtistId(userId, artistId, cursor);
        return ApiResponseUtil.success(SuccessMessage.SUCCESS, PerformanceByArtistResponse.of(performances, s3FileHandler));
    }

    @GetMapping("/info")
    public ResponseEntity<BaseResponse<?>> getRecentPerformances(
            @RequestHeader(name = "Authorization", required = false) Long userId
    ) {
        RecentPerformancesDTO recentPerformances = performanceFacade.getRecentPerformances(userId);
        return ApiResponseUtil.success(SuccessMessage.SUCCESS, RecentPerformancesResponse.of(recentPerformances, s3FileHandler));
    }
}
