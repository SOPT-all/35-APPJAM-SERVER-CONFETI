package org.sopt.confeti.api.performance.controller;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import java.util.Arrays;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.sopt.confeti.api.performance.controller.docs.PerformanceControllerDocs;
import org.sopt.confeti.api.performance.dto.request.GetUpcomingPerformanceRequest;
import org.sopt.confeti.api.performance.dto.response.ArtistPerformancesResponse;
import org.sopt.confeti.api.performance.dto.response.ConcertDetailResponse;
import org.sopt.confeti.api.performance.dto.response.ConfetiRecordResponse;
import org.sopt.confeti.api.performance.dto.response.FestivalDetailResponse;
import org.sopt.confeti.api.performance.dto.response.PerformanceIdsResponse;
import org.sopt.confeti.api.performance.dto.response.PerformanceReservationResponse;
import org.sopt.confeti.api.performance.dto.response.PerformancesRecommendResponse;
import org.sopt.confeti.api.performance.dto.response.RecentPerformancesResponse;
import org.sopt.confeti.api.performance.dto.response.RecommendPerformancesResponse;
import org.sopt.confeti.api.performance.dto.response.SearchACPerformancesResponse;
import org.sopt.confeti.api.performance.dto.response.UpcomingPerformancesResponse;
import org.sopt.confeti.api.performance.facade.PerformanceFacade;
import org.sopt.confeti.api.performance.facade.dto.request.GetUpcomingPerformancesDTO;
import org.sopt.confeti.api.performance.facade.dto.response.ArtistPerformancesDTO;
import org.sopt.confeti.api.performance.facade.dto.response.ConcertDetailWithFavoriteDTO;
import org.sopt.confeti.api.performance.facade.dto.response.ConfetiRecordDTO;
import org.sopt.confeti.api.performance.facade.dto.response.FestivalDetailWithFavoriteDTO;
import org.sopt.confeti.api.performance.facade.dto.response.PerformanceIdsDTO;
import org.sopt.confeti.api.performance.facade.dto.response.PerformanceReservationDTO;
import org.sopt.confeti.api.performance.facade.dto.response.PerformancesRecommendDTO;
import org.sopt.confeti.api.performance.facade.dto.response.RecentPerformancesDTO;
import org.sopt.confeti.api.performance.facade.dto.response.RecommendPerformancesDTO;
import org.sopt.confeti.api.performance.facade.dto.response.SearchACPerformancesDTO;
import org.sopt.confeti.api.performance.facade.dto.response.UpcomingPerformancesDTO;
import org.sopt.confeti.domain.user.constant.Role;
import org.sopt.confeti.global.annotation.ApiVersion;
import org.sopt.confeti.global.annotation.Permission;
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
@RequestMapping("/performances")
public class PerformanceController implements PerformanceControllerDocs {

    private final PerformanceFacade performanceFacade;
    private final S3FileHandler s3FileHandler;

    @GetMapping("/concerts/{concertId}")
    public ResponseEntity<BaseResponse<ConcertDetailResponse>> getConcertInfo(
        @PathVariable("concertId") @Min(RequestConstraint.ID) long concertId
    ) {
        ConcertDetailWithFavoriteDTO concertDetail = performanceFacade.getUpcomingConcertDetail(
            concertId);
        return ApiResponseUtil.success(SuccessMessage.SUCCESS,
            ConcertDetailResponse.of(concertDetail, s3FileHandler));
    }

    @GetMapping("/festivals/{festivalId}")
    public ResponseEntity<BaseResponse<FestivalDetailResponse>> getFestivalInfo(
        @PathVariable("festivalId") @Min(RequestConstraint.ID) Long festivalId
    ) {
        FestivalDetailWithFavoriteDTO festivalDetail = performanceFacade.getUpcomingFestivalDetail(
            festivalId);
        return ApiResponseUtil.success(SuccessMessage.SUCCESS,
            FestivalDetailResponse.from(festivalDetail));
    }

    @GetMapping("/reservation")
    public ResponseEntity<BaseResponse<PerformanceReservationResponse>> getPerformReservationInfo() {
        PerformanceReservationDTO performanceReservationDTO = performanceFacade.getPerformanceReservationInfo();
        return ApiResponseUtil.success(SuccessMessage.SUCCESS,
            PerformanceReservationResponse.from(performanceReservationDTO));
    }

    @GetMapping("/association/{artistId}")
    public ResponseEntity<BaseResponse<ArtistPerformancesResponse>> getPerformanceByArtist(
        @PathVariable(name = "artistId") String artistId
    ) {
        ArtistPerformancesDTO performances = performanceFacade.getPerformancesByArtistId(artistId);
        return ApiResponseUtil.success(SuccessMessage.SUCCESS,
            ArtistPerformancesResponse.from(performances));
    }

    @GetMapping("/info")
    public ResponseEntity<BaseResponse<RecentPerformancesResponse>> getRecentPerformances(
    ) {
        RecentPerformancesDTO recentPerformances = performanceFacade.getRecentPerformances();
        return ApiResponseUtil.success(SuccessMessage.SUCCESS,
            RecentPerformancesResponse.from(recentPerformances));
    }

    @ApiVersion("2")
    @GetMapping("/recommend")
    public ResponseEntity<BaseResponse<RecommendPerformancesResponse>> getRecommendPerformances(
        @RequestParam(defaultValue = "5") int limit
    ) {
        RecommendPerformancesDTO recommendPerformances = performanceFacade.getRecommendPerformances(
            limit);
        return ApiResponseUtil.success(SuccessMessage.SUCCESS,
            RecommendPerformancesResponse.from(recommendPerformances));
    }

    @GetMapping("/search/ac")
    public ResponseEntity<BaseResponse<SearchACPerformancesResponse>> searchAutoComplete(
        @RequestParam @NotBlank String term,
        @RequestParam(required = false, defaultValue = "1") @Min(1) @Max(10) Integer limit,
        @RequestParam(required = false, defaultValue = Default.PERFORMANCE_STATUS) PerformanceStatus status
    ) {
        SearchACPerformancesDTO performancesDTO = performanceFacade.searchACPerformances(term,
            limit,
            status);
        return ApiResponseUtil.success(SuccessMessage.SUCCESS,
            SearchACPerformancesResponse.from(performancesDTO));
    }

    @ApiVersion("2")
    @GetMapping("/song/recommend")
    public ResponseEntity<BaseResponse<PerformancesRecommendResponse>> getSongRecommend(
        @RequestParam(defaultValue = "3") Integer performanceLimit,
        @RequestParam(defaultValue = "3") Integer songLimit
    ) {
        PerformancesRecommendDTO performancesRecommendDTO = performanceFacade.getPerformancesRecommend(
            performanceLimit, songLimit);
        return ApiResponseUtil.success(SuccessMessage.SUCCESS,
            PerformancesRecommendResponse.from(performancesRecommendDTO)
        );
    }

    @Permission(role = {Role.GENERAL})
    @GetMapping("/record")
    public ResponseEntity<BaseResponse<ConfetiRecordResponse>> getConfetiRecord(
    ) {
        ConfetiRecordDTO recordDTO = performanceFacade.getConfetiRecord();
        return ApiResponseUtil.success(SuccessMessage.SUCCESS,
            ConfetiRecordResponse.from(recordDTO));
    }

    @GetMapping("/upcoming")
    public ResponseEntity<BaseResponse<UpcomingPerformancesResponse>> getUpcomingPerformances(
        @RequestParam String items
    ) {
        List<GetUpcomingPerformanceRequest> performanceRequests = decodeToUpcomingPerformancesRequest(
            items);
        UpcomingPerformancesDTO upcomingPerformances = performanceFacade.getUpcomingPerformances(
            GetUpcomingPerformancesDTO.from(performanceRequests));
        return ApiResponseUtil.success(SuccessMessage.SUCCESS,
            UpcomingPerformancesResponse.from(upcomingPerformances));
    }

    private List<GetUpcomingPerformanceRequest> decodeToUpcomingPerformancesRequest(
        String request) {
        try {
            return Arrays.stream(request.split(","))
                .map(item -> {
                    String[] performance = item.split(":");
                    return GetUpcomingPerformanceRequest.of(
                        PerformanceType.convert(performance[0].trim()),
                        Long.parseLong(performance[1].trim())
                    );
                })
                .toList();
        } catch (Exception e) {
            throw new ConfetiException(ErrorMessage.BAD_REQUEST);
        }
    }

    @GetMapping
    public ResponseEntity<BaseResponse<PerformanceIdsResponse>> getPerformances() {
        PerformanceIdsDTO performances = performanceFacade.getPerformances();
        return ApiResponseUtil.success(SuccessMessage.SUCCESS,
            PerformanceIdsResponse.from(performances));
    }
}
