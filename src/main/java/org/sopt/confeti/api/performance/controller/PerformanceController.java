package org.sopt.confeti.api.performance.controller;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.sopt.confeti.api.performance.dto.response.AnalyzePerformanceTypeResponse;
import org.sopt.confeti.api.performance.dto.response.ArtistPerformancesResponse;
import org.sopt.confeti.api.performance.dto.response.ConcertDetailResponse;
import org.sopt.confeti.api.performance.dto.response.ConfetiRecordResponse;
import org.sopt.confeti.api.performance.dto.response.FestivalDetailResponse;
import org.sopt.confeti.api.performance.dto.response.IntendedPerformancesResponse;
import org.sopt.confeti.api.performance.dto.response.PerformanceReservationResponse;
import org.sopt.confeti.api.performance.dto.response.RecentPerformancesResponse;
import org.sopt.confeti.api.performance.dto.response.RecommendMusicsResponse;
import org.sopt.confeti.api.performance.dto.response.RecommendNewMusicsResponse;
import org.sopt.confeti.api.performance.dto.response.RecommendPerformancesResponse;
import org.sopt.confeti.api.performance.dto.response.SearchACPerformancesResponse;
import org.sopt.confeti.api.performance.facade.PerformanceFacade;
import org.sopt.confeti.api.performance.facade.dto.response.AnalyzePerformanceTypeDTO;
import org.sopt.confeti.api.performance.facade.dto.response.ArtistPerformancesDTO;
import org.sopt.confeti.api.performance.facade.dto.response.ConcertDetailDTO;
import org.sopt.confeti.api.performance.facade.dto.response.ConfetiRecordDTO;
import org.sopt.confeti.api.performance.facade.dto.response.FestivalDetailDTO;
import org.sopt.confeti.api.performance.facade.dto.response.IntendedPerformancesDTO;
import org.sopt.confeti.api.performance.facade.dto.response.PerformanceReservationDTO;
import org.sopt.confeti.api.performance.facade.dto.response.RecentPerformancesDTO;
import org.sopt.confeti.api.performance.facade.dto.response.RecommendMusicsDTO;
import org.sopt.confeti.api.performance.facade.dto.response.RecommendNewMusicsDTO;
import org.sopt.confeti.api.performance.facade.dto.response.RecommendPerformancesDTO;
import org.sopt.confeti.api.performance.facade.dto.response.SearchACPerformancesDTO;
import org.sopt.confeti.domain.user.constant.Role;
import org.sopt.confeti.global.annotation.Permission;
import org.sopt.confeti.global.annotation.UserId;
import org.sopt.confeti.global.common.BaseResponse;
import org.sopt.confeti.global.common.constant.Default;
import org.sopt.confeti.global.common.constant.PerformanceStatus;
import org.sopt.confeti.global.common.constant.PerformanceType;
import org.sopt.confeti.global.common.constant.RequestConstraint;
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

import java.util.List;

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
    @GetMapping("/search")
    public ResponseEntity<BaseResponse<?>> search(
            @UserId(require = false) Long userId,
            @RequestParam(required = false) Long pid,
            @RequestParam(required = false) String aid,
            @RequestParam(required = false) String ptitle,
            @RequestParam(required = false, defaultValue = Default.PERFORMANCE_TYPE) PerformanceType ptype
    ) {
        IntendedPerformancesDTO performancesDTO = performanceFacade.getPerformances(userId, pid, aid, ptitle, ptype);
        return ApiResponseUtil.success(SuccessMessage.SUCCESS,
                IntendedPerformancesResponse.of(performancesDTO, s3FileHandler));
    }

    @Permission(role = {Role.GENERAL})
    @GetMapping("/search/type-analysis")
    public ResponseEntity<BaseResponse<?>> analyzePerformanceType(
            @UserId(require = false) Long userId,
            @RequestParam String term
    ) {
        AnalyzePerformanceTypeDTO analyzePerformanceTypeDTO = performanceFacade.analyzePerformanceType(term);
        return ApiResponseUtil.success(SuccessMessage.SUCCESS,
                AnalyzePerformanceTypeResponse.from(analyzePerformanceTypeDTO));
    }

    @Permission(role = {Role.GENERAL})
    @GetMapping("/recommend/musics")
    public ResponseEntity<BaseResponse<?>> getRecommendMusics(
            @UserId(require = false) Long userId
    ) {
        RecommendMusicsDTO recommendMusicsDTO = performanceFacade.getRecommendMusics(userId);
        return ApiResponseUtil.success(SuccessMessage.SUCCESS,
                RecommendMusicsResponse.from(recommendMusicsDTO));
    }

    @Permission(role = {Role.GENERAL})
    @GetMapping("/recommend/musics")
    public ResponseEntity<BaseResponse<?>> getRecommendMusics(
            @RequestParam Long performanceId,
            @RequestParam(required = false) List<String> musicIds
    ) {
        RecommendNewMusicsDTO recommendMusicsDTO = performanceFacade.getNewRecommendMusics(performanceId, musicIds);
        return ApiResponseUtil.success(SuccessMessage.SUCCESS,
                RecommendNewMusicsResponse.from(recommendMusicsDTO));
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
}
