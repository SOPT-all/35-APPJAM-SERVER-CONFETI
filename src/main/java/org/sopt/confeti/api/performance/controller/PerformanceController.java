package org.sopt.confeti.api.performance.controller;

import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.sopt.confeti.api.performance.dto.response.ArtistPerformancesResponse;
import org.sopt.confeti.api.performance.dto.response.ConcertDetailResponse;
import org.sopt.confeti.api.performance.dto.response.FestivalDetailResponse;
import org.sopt.confeti.api.performance.dto.response.PerformanceReservationResponse;
import org.sopt.confeti.api.performance.dto.response.RecentPerformancesResponse;
import org.sopt.confeti.api.performance.facade.PerformanceFacade;
import org.sopt.confeti.api.performance.facade.dto.response.ArtistPerformancesDTO;
import org.sopt.confeti.api.performance.facade.dto.response.ConcertDetailDTO;
import org.sopt.confeti.api.performance.facade.dto.response.FestivalDetailDTO;
import org.sopt.confeti.api.performance.facade.dto.response.PerformanceReservationDTO;
import org.sopt.confeti.api.performance.facade.dto.response.RecentPerformancesDTO;
import org.sopt.confeti.domain.user.constant.Role;
import org.sopt.confeti.global.annotation.Permission;
import org.sopt.confeti.global.annotation.UserId;
import org.sopt.confeti.global.common.BaseResponse;
import org.sopt.confeti.global.common.constant.RequestConstraint;
import org.sopt.confeti.global.message.SuccessMessage;
import org.sopt.confeti.global.util.ApiResponseUtil;
import org.sopt.confeti.global.util.S3FileHandler;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping("/performances")
public class PerformanceController {

    private final PerformanceFacade performanceFacade;
    private final S3FileHandler s3FileHandler;

    @Permission(role = {Role.GENERAL})
    @GetMapping("/concerts/{concertId}")
    public ResponseEntity<BaseResponse<?>> getConcertInfo(
            @UserId(require = false) Long userId,
            @PathVariable("concertId") @Min(RequestConstraint.ID) long concertId
    ) {
        ConcertDetailDTO concertDetailDTO = performanceFacade.getConcertDetailInfo(userId, concertId);
        return ApiResponseUtil.success(SuccessMessage.SUCCESS,
                ConcertDetailResponse.of(concertDetailDTO, s3FileHandler));
    }

    @Permission(role = {Role.GENERAL})
    @GetMapping("/festivals/{festivalId}")
    public ResponseEntity<BaseResponse<?>> getFestivalInfo(
            @UserId(require = false) Long userId,
            @PathVariable("festivalId") @Min(RequestConstraint.ID) Long festivalId
    ) {
        FestivalDetailDTO festivalDetailDTO = performanceFacade.getFestivalDetailInfo(userId, festivalId);
        return ApiResponseUtil.success(SuccessMessage.SUCCESS,
                FestivalDetailResponse.of(festivalDetailDTO, s3FileHandler));
    }

    @Permission(role = {Role.GENERAL})
    @GetMapping("/reservation")
    public ResponseEntity<BaseResponse<?>> getPerformReservationInfo(
            @UserId(require = false) Long userId
    ) {
        PerformanceReservationDTO performanceReservationDTO = performanceFacade.getPerformReservationInfo(userId);
        return ApiResponseUtil.success(SuccessMessage.SUCCESS,
                PerformanceReservationResponse.from(performanceReservationDTO));
    }

    @Permission(role = {Role.GENERAL})
    @GetMapping("/association/{artistId}")
    public ResponseEntity<BaseResponse<?>> getPerformanceByArtist(
            @UserId(require = false) Long userId,
            @PathVariable(name = "artistId") String artistId
    ) {
        ArtistPerformancesDTO performances = performanceFacade.getPerformancesByArtistId(userId, artistId);
        return ApiResponseUtil.success(SuccessMessage.SUCCESS,
                ArtistPerformancesResponse.of(performances, s3FileHandler));
    }

    @Permission(role = {Role.GENERAL})
    @GetMapping("/info")
    public ResponseEntity<BaseResponse<?>> getRecentPerformances(
            @UserId(require = false) Long userId
    ) {
        RecentPerformancesDTO recentPerformances = performanceFacade.getRecentPerformances(userId);
        return ApiResponseUtil.success(SuccessMessage.SUCCESS,
                RecentPerformancesResponse.of(recentPerformances, s3FileHandler));
    }
}
