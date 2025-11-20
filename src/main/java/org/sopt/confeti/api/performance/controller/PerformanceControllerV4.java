package org.sopt.confeti.api.performance.controller;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import java.util.Arrays;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.sopt.confeti.api.performance.controller.docs.PerformanceControllerV4Docs;
import org.sopt.confeti.api.performance.dto.request.GetExpectedPerformanceRequest;
import org.sopt.confeti.api.performance.dto.response.ArtistPerformancesResponse;
import org.sopt.confeti.api.performance.dto.response.ConcertDetailResponse;
import org.sopt.confeti.api.performance.dto.response.ConfetiRecordResponse;
import org.sopt.confeti.api.performance.dto.response.ExpectedPerformancesResponse;
import org.sopt.confeti.api.performance.dto.response.FestivalDetailResponse;
import org.sopt.confeti.api.performance.dto.response.PerformanceIdsResponse;
import org.sopt.confeti.api.performance.dto.response.PerformanceReservationResponse;
import org.sopt.confeti.api.performance.dto.response.PerformancesRecommendResponse;
import org.sopt.confeti.api.performance.dto.response.RecentPerformancesResponse;
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
import org.sopt.confeti.api.performance.facade.dto.response.PerformancesRecommendDTO;
import org.sopt.confeti.api.performance.facade.dto.response.RecentPerformancesDTO;
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
@RequestMapping("/performances/v4")
public class PerformanceControllerV4 implements PerformanceControllerV4Docs {

    private final PerformanceFacade performanceFacade;
    private final S3FileHandler s3FileHandler;

    @Permission(role = {Role.GENERAL})
    @GetMapping("/concerts/{concertId}")
    public ResponseEntity<BaseResponse<ConcertDetailResponse>> getConcertInfo(
            @UserId(require = false) Long userId,
            @PathVariable("concertId") @Min(RequestConstraint.ID) long concertId
    ) {
        ConcertDetailDTO concertDetailDTO = performanceFacade.getConcertDetailInfo(userId, concertId);
        return ApiResponseUtil.success(SuccessMessage.SUCCESS,
                ConcertDetailResponse.of(concertDetailDTO, s3FileHandler));
    }

    @Permission(role = {Role.GENERAL})
    @GetMapping("/festivals/{festivalId}")
    public ResponseEntity<BaseResponse<FestivalDetailResponse>> getFestivalInfo(
            @UserId(require = false) Long userId,
            @PathVariable("festivalId") @Min(RequestConstraint.ID) Long festivalId
    ) {
        FestivalDetailDTO festivalDetailDTO = performanceFacade.getFestivalDetailInfo(userId, festivalId);
        return ApiResponseUtil.success(SuccessMessage.SUCCESS,
                FestivalDetailResponse.of(festivalDetailDTO, s3FileHandler));
    }

    @Permission(role = {Role.GENERAL})
    @GetMapping("/reservation")
    public ResponseEntity<BaseResponse<PerformanceReservationResponse>> getPerformReservationInfo(
            @UserId(require = false) Long userId
    ) {
        PerformanceReservationDTO performanceReservationDTO = performanceFacade.getPerformReservationInfo(userId);
        return ApiResponseUtil.success(SuccessMessage.SUCCESS,
                PerformanceReservationResponse.from(performanceReservationDTO));
    }

    @Permission(role = {Role.GENERAL})
    @GetMapping("/association/{artistId}")
    public ResponseEntity<BaseResponse<ArtistPerformancesResponse>> getPerformanceByArtist(
            @UserId(require = false) Long userId,
            @PathVariable(name = "artistId") String artistId
    ) {
        ArtistPerformancesDTO performances = performanceFacade.getPerformancesByArtistId(userId, artistId);
        return ApiResponseUtil.success(SuccessMessage.SUCCESS,
                ArtistPerformancesResponse.of(performances, s3FileHandler));
    }

    @Permission(role = {Role.GENERAL})
    @GetMapping("/info")
    public ResponseEntity<BaseResponse<RecentPerformancesResponse>> getRecentPerformances(
            @UserId(require = false) Long userId
    ) {
        RecentPerformancesDTO recentPerformances = performanceFacade.getRecentPerformances(userId);
        return ApiResponseUtil.success(SuccessMessage.SUCCESS,
                RecentPerformancesResponse.of(recentPerformances, s3FileHandler));
    }

    @Permission(role = {Role.GENERAL})
    @GetMapping("/recommend")
    public ResponseEntity<BaseResponse<RecommendPerformancesResponse>> getRecommendPerformances(
            @RequestParam(defaultValue = "5") @Min(1) @Max(20) int limit
    ) {
        RecommendPerformancesDTO recommendPerformances = performanceFacade.getRecommendPerformances(limit);
        return ApiResponseUtil.success(SuccessMessage.SUCCESS,
                RecommendPerformancesResponse.of(recommendPerformances, s3FileHandler));
    }

    @Permission(role = {Role.GENERAL})
    @GetMapping("/search/ac")
    public ResponseEntity<BaseResponse<SearchACPerformancesResponse>> searchAutoComplete(
            @UserId(require = false) Long userId,
            @RequestParam @NotBlank  String term,
            @RequestParam(required = false, defaultValue = "1") @Min(1) @Max(10) Integer limit,
            @RequestParam(required = false, defaultValue = Default.PERFORMANCE_STATUS) PerformanceStatus status
    ) {
        SearchACPerformancesDTO performancesDTO = performanceFacade.searchACPerformances(term, limit,
                status);
        return ApiResponseUtil.success(SuccessMessage.SUCCESS,
                SearchACPerformancesResponse.of(performancesDTO, s3FileHandler));
    }

    @Permission(role = {Role.GENERAL})
    @GetMapping("/song/recommend")
    public ResponseEntity<BaseResponse<PerformancesRecommendResponse>> getSongRecommend(
            @RequestParam(defaultValue = "3") @Min(1) @Max(5) Integer performanceLimit,
            @RequestParam(defaultValue = "3") @Min(1) @Max(5) Integer songLimit
    ) {
        PerformancesRecommendDTO performancesRecommendDTO = performanceFacade.getPerformancesRecommend(performanceLimit, songLimit);
        return ApiResponseUtil.success(SuccessMessage.SUCCESS,
                PerformancesRecommendResponse.of(performancesRecommendDTO, s3FileHandler)
        );
    }

    @Permission(role = {Role.GENERAL})
    @GetMapping("/record")
    public ResponseEntity<BaseResponse<ConfetiRecordResponse>> getConfetiRecord(
            @UserId Long userId
    ) {
        ConfetiRecordDTO recordDTO = performanceFacade.getConfetiRecord(userId);
        return ApiResponseUtil.success(SuccessMessage.SUCCESS,
                ConfetiRecordResponse.from(recordDTO));
    }

    @Permission(role = {Role.GENERAL})
    @GetMapping("/expected")
    public ResponseEntity<BaseResponse<ExpectedPerformancesResponse>> getExpectedPerformances(
            @UserId(require = false) Long userId,
            @RequestParam String items
    ) {
        List<GetExpectedPerformanceRequest> performanceRequests = decodeToExpectedPerformancesRequest(items);
        ExpectedPerformancesDTO expectedPerformances = performanceFacade.getExpectedPerformances(
                GetExpectedPerformancesDTO.from(performanceRequests));
        return ApiResponseUtil.success(SuccessMessage.SUCCESS,
                ExpectedPerformancesResponse.of(expectedPerformances, s3FileHandler));
    }

    private List<GetExpectedPerformanceRequest> decodeToExpectedPerformancesRequest(String request) {
        try {
            return Arrays.stream(request.split(","))
                    .map(item -> {
                        String[] performance = item.split(":");
                        return GetExpectedPerformanceRequest.of(
                                PerformanceType.convert(performance[0].trim()),
                                Long.parseLong(performance[1].trim())
                        );
                    })
                    .toList();
        } catch (Exception e) {
            throw new ConfetiException(ErrorMessage.BAD_REQUEST);
        }
    }

    @Permission(role = {Role.GENERAL})
    @GetMapping
    public ResponseEntity<BaseResponse<PerformanceIdsResponse>> getPerformances() {
        PerformanceIdsDTO performances = performanceFacade.getPerformances();
        return ApiResponseUtil.success(SuccessMessage.SUCCESS,
                PerformanceIdsResponse.from(performances));
    }
}
