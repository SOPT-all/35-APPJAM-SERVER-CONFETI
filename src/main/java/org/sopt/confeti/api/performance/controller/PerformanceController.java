package org.sopt.confeti.api.performance.controller;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.sopt.confeti.api.performance.dto.request.GetExpectedPerformanceRequest;
import org.sopt.confeti.api.performance.dto.response.ArtistPerformancesResponse;
import org.sopt.confeti.api.performance.dto.response.ConcertDetailResponse;
import org.sopt.confeti.api.performance.dto.response.ConfetiRecordResponse;
import org.sopt.confeti.api.performance.dto.response.ExpectedPerformancesResponse;
import org.sopt.confeti.api.performance.dto.response.FestivalDetailResponse;
import org.sopt.confeti.api.performance.dto.response.PerformanceIdsResponse;
import org.sopt.confeti.api.performance.dto.response.PerformanceReservationResponse;
import org.sopt.confeti.api.performance.dto.response.RecentPerformancesResponse;
import org.sopt.confeti.api.performance.dto.response.RecommendMusicsPerformanceResponse;
import org.sopt.confeti.api.performance.dto.response.RecommendMusicsResponse;
import org.sopt.confeti.api.performance.dto.response.RecommendPerformancesResponse;
import org.sopt.confeti.api.performance.dto.response.SearchACPerformancesResponse;
import org.sopt.confeti.api.performance.facade.PerformanceFacade;
import org.sopt.confeti.api.performance.facade.dto.request.GetExpectedPerformancesDTO;
import org.sopt.confeti.api.performance.facade.dto.response.ArtistPerformancesDTO;
import org.sopt.confeti.api.performance.facade.dto.response.ConcertDetailDTO;
import org.sopt.confeti.api.performance.facade.dto.response.ConfetiRecordDTO;
import org.sopt.confeti.api.performance.facade.dto.response.ExpectedPerformancesDTO;
import org.sopt.confeti.api.performance.facade.dto.response.FestivalDetailDTO;
import org.sopt.confeti.api.performance.facade.dto.response.PerformanceIdsDTO;
import org.sopt.confeti.api.performance.facade.dto.response.PerformanceReservationDTO;
import org.sopt.confeti.api.performance.facade.dto.response.RecentPerformancesDTO;
import org.sopt.confeti.api.performance.facade.dto.response.RecommendMusicsDTO;
import org.sopt.confeti.api.performance.facade.dto.response.RecommendMusicsPerformanceDTO;
import org.sopt.confeti.api.performance.facade.dto.response.RecommendPerformancesDTO;
import org.sopt.confeti.api.performance.facade.dto.response.SearchACPerformancesDTO;
import org.sopt.confeti.domain.performance.PerformanceType;
import org.sopt.confeti.domain.user.constant.Role;
import org.sopt.confeti.global.annotation.Permission;
import org.sopt.confeti.global.annotation.UserId;
import org.sopt.confeti.global.common.BaseResponse;
import org.sopt.confeti.global.common.constant.Default;
import org.sopt.confeti.global.common.constant.PerformanceStatus;
import org.sopt.confeti.global.common.constant.PerformanceType_DEPRECATED;
import org.sopt.confeti.global.common.constant.RequestConstraint;
import org.sopt.confeti.global.exception.ConfetiException;
import org.sopt.confeti.global.message.ErrorMessage;
import org.sopt.confeti.global.message.SuccessMessage;
import org.sopt.confeti.global.util.ApiResponseUtil;
import org.sopt.confeti.global.util.S3FileHandler;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping("/performances")
public class PerformanceController {

    private final PerformanceFacade performanceFacade;
    private final S3FileHandler s3FileHandler;

    @Permission(role = {Role.GENERAL})
    @GetMapping("/concerts/{performanceId}")
    public ResponseEntity<BaseResponse<?>> getConcertInfo(
            @UserId(require = false) Long userId,
            @PathVariable @Min(RequestConstraint.ID) long performanceId
    ) {
        ConcertDetailDTO concertDetailDTO = performanceFacade.getConcertDetail(userId, performanceId);
        return ApiResponseUtil.success(SuccessMessage.SUCCESS,
                ConcertDetailResponse.from(concertDetailDTO));
    }

    @Permission(role = {Role.GENERAL})
    @GetMapping("/festivals/{performanceId}")
    public ResponseEntity<BaseResponse<?>> getFestivalInfo(
            @UserId(require = false) Long userId,
            @PathVariable @Min(RequestConstraint.ID) Long performanceId
    ) {
        FestivalDetailDTO festivalDetailDTO = performanceFacade.getFestivalDetail(userId, performanceId);
        return ApiResponseUtil.success(SuccessMessage.SUCCESS,
                FestivalDetailResponse.from(festivalDetailDTO));
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

    @Permission(role = {Role.GENERAL})
    @GetMapping("/recommend")
    public ResponseEntity<BaseResponse<?>> getRecommendPerformances(
    ) {
        RecommendPerformancesDTO recommendPerformances = performanceFacade.getRecommendPerformances();
        return ApiResponseUtil.success(SuccessMessage.SUCCESS,
                RecommendPerformancesResponse.of(recommendPerformances, s3FileHandler));
    }

    @Permission(role = {Role.GENERAL})
    @GetMapping("/search/ac")
    public ResponseEntity<BaseResponse<?>> searchAutoComplete(
            @UserId(require = false) Long userId,
            @RequestParam @NotBlank String term,
            @RequestParam(required = false, defaultValue = "1") @Min(1) @Max(10) Integer limit,
            @RequestParam(required = false, defaultValue = Default.PERFORMANCE_STATUS) String status
    ) {
        SearchACPerformancesDTO performancesDTO = performanceFacade.searchACPerformances(term, limit,
                PerformanceStatus.convert(status));
        return ApiResponseUtil.success(SuccessMessage.SUCCESS,
                SearchACPerformancesResponse.of(performancesDTO, s3FileHandler));
    }

    @Permission(role = {Role.GENERAL})
    @GetMapping("/recommend/performance")
    public ResponseEntity<BaseResponse<?>> getRecommendExpectedPerformanceId(
            @UserId(require = false) Long userId
    ) {
        Optional<RecommendMusicsPerformanceDTO> recommendMusicsDTO = performanceFacade.getRecommendExpectedPerformanceId(
                userId);
        return ApiResponseUtil.success(SuccessMessage.SUCCESS,
                recommendMusicsDTO.map(RecommendMusicsPerformanceResponse::from).orElse(null)
        );
    }

    @Permission(role = {Role.GENERAL})
    @GetMapping("/recommend/musics")
    public ResponseEntity<BaseResponse<?>> getRecommendMusics(
            @RequestParam Long performanceId,
            @RequestParam(required = false) List<String> musicIds
    ) {
        RecommendMusicsDTO recommendMusicsDTO = performanceFacade.getNewRecommendMusics(performanceId, musicIds);
        return ApiResponseUtil.success(SuccessMessage.SUCCESS,
                RecommendMusicsResponse.from(recommendMusicsDTO));
    }

    @Permission(role = {Role.GENERAL})
    @GetMapping("/record")
    public ResponseEntity<BaseResponse<?>> getConfetiRecord(
            @UserId Long userId
    ) {
        ConfetiRecordDTO recordDTO = performanceFacade.getConfetiRecord(userId);
        return ApiResponseUtil.success(SuccessMessage.SUCCESS,
                ConfetiRecordResponse.from(recordDTO));
    }

    @Permission(role = {Role.GENERAL})
    @GetMapping("/expected")
    public ResponseEntity<BaseResponse<?>> getExpectedPerformances(
            @UserId(require = false) Long userId,
            @RequestParam String performanceIds
    ) {
        List<GetExpectedPerformanceRequest> performanceRequests = decodeToExpectedPerformancesRequest(performanceIds);
        ExpectedPerformancesDTO expectedPerformances = performanceFacade.getExpectedPerformances(
                GetExpectedPerformancesDTO.from(performanceRequests));
        return ApiResponseUtil.success(SuccessMessage.SUCCESS,
                ExpectedPerformancesResponse.from(expectedPerformances));
    }

    private List<GetExpectedPerformanceRequest> decodeToExpectedPerformancesRequest(String performanceIds) {
        return Arrays.stream(performanceIds.split(","))
                .map(Long::valueOf)
                .map(GetExpectedPerformanceRequest::from)
                .toList();
    }

    @Permission(role = {Role.GENERAL})
    @GetMapping
    public ResponseEntity<BaseResponse<?>> getPerformances() {
        PerformanceIdsDTO performances = performanceFacade.getPerformances();
        return ApiResponseUtil.success(SuccessMessage.SUCCESS,
                PerformanceIdsResponse.from(performances));
    }
}
