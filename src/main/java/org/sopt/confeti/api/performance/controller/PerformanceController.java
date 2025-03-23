package org.sopt.confeti.api.performance.controller;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.sopt.confeti.api.performance.dto.request.CreateConcertRequest;
import org.sopt.confeti.api.performance.dto.request.CreateFestivalRequest;
import org.sopt.confeti.api.performance.dto.response.*;
import org.sopt.confeti.api.performance.facade.PerformanceFacade;
import org.sopt.confeti.api.performance.facade.dto.request.CreateConcertDTO;
import org.sopt.confeti.api.performance.facade.dto.request.CreateConcertFileDTO;
import org.sopt.confeti.api.performance.facade.dto.request.CreateFestivalDTO;
import org.sopt.confeti.api.performance.facade.dto.request.CreateFestivalFileDTO;
import org.sopt.confeti.api.performance.facade.dto.response.*;
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
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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
        return ApiResponseUtil.success(SuccessMessage.SUCCESS, ConcertDetailResponse.of(concertDetailDTO, s3FileHandler));
    }

    @Permission(role = {Role.GENERAL})
    @GetMapping("/festivals/{festivalId}")
    public ResponseEntity<BaseResponse<?>> getFestivalInfo(
            @UserId(require = false) Long userId,
            @PathVariable("festivalId") @Min(RequestConstraint.ID) Long festivalId
    ) {
        FestivalDetailDTO festivalDetailDTO = performanceFacade.getFestivalDetailInfo(userId, festivalId);
        return ApiResponseUtil.success(SuccessMessage.SUCCESS, FestivalDetailResponse.from(festivalDetailDTO));
    }

    // TODO: 관리자 검증 로직 추가, 더미 데이터 추가 로직 개선
    @Permission(role = {Role.ADMIN})
    @PostMapping("/concerts")
    public ResponseEntity<BaseResponse<?>> createConcert(
            @UserId Long userId,
            @RequestBody CreateConcertRequest createConcertRequest
    ) {
        performanceFacade.createConcert(CreateConcertDTO.from(createConcertRequest));
        return ApiResponseUtil.success(SuccessMessage.CREATED);
    }

    // TODO: 관리자 검증 로직 추가, 더미 데이터 추가 로직 개선
    @Permission(role = {Role.ADMIN})
    @PostMapping("/concerts/{concertId}/files")
    public ResponseEntity<BaseResponse<?>> createConcertFiles(
            @UserId Long userId,
            @PathVariable("concertId") @Min(RequestConstraint.ID) long concertId,
            @RequestPart("poster") @NotNull MultipartFile poster,
            @RequestPart("posterBg") @NotNull MultipartFile posterBg,
            @RequestPart("infoImg") @NotNull MultipartFile infoImg,
            @RequestPart("reservationBg") @NotNull MultipartFile reservationBg
    ) {
        performanceFacade.createConcertFiles(concertId, CreateConcertFileDTO.of(poster, posterBg, infoImg, reservationBg));
        return ApiResponseUtil.success(SuccessMessage.CREATED);
    }

    // TODO: 관리자 검증 로직 추가, 더미 데이터 추가 로직 개선
    @Permission(role = {Role.ADMIN})
    @PostMapping("/festivals")
    public ResponseEntity<BaseResponse<?>> createFestival(
            @UserId Long userId,
            @RequestBody CreateFestivalRequest createFestivalRequest
    ) {
        performanceFacade.createFestival(CreateFestivalDTO.from(createFestivalRequest));

        return ApiResponseUtil.success(SuccessMessage.CREATED);
    }

    // TODO: 관리자 검증 로직 추가, 더미 데이터 추가 로직 개선
    @Permission(role = {Role.ADMIN})
    @PostMapping("/festivals/{festivalId}/files")
    public ResponseEntity<BaseResponse<?>> createFestivalFiles(
            @UserId Long userId,
            @PathVariable("festivalId") @Min(RequestConstraint.ID) long festivalId,
            @RequestPart("poster") @NotNull MultipartFile poster,
            @RequestPart("posterBg") @NotNull MultipartFile posterBg,
            @RequestPart("infoImg") @NotNull MultipartFile infoImg,
            @RequestPart("reservationBg") @NotNull MultipartFile reservationBg,
            @RequestPart("logo") @NotNull MultipartFile logo
    ) {
        performanceFacade.createFestivalFiles(festivalId, CreateFestivalFileDTO.of(poster, posterBg, infoImg, reservationBg, logo));
        return ApiResponseUtil.success(SuccessMessage.CREATED);
    }

    @Permission(role = {Role.GENERAL})
    @GetMapping("/reservation")
    public ResponseEntity<BaseResponse<?>> getPerformReservationInfo(
            @UserId(require = false) Long userId
    ) {
        PerformanceReservationDTO performanceReservationDTO = performanceFacade.getPerformReservationInfo(userId);
        return ApiResponseUtil.success(SuccessMessage.SUCCESS, PerformanceReservationResponse.of(performanceReservationDTO, s3FileHandler));
    }

    @Permission(role = {Role.GENERAL})
    @GetMapping("/association/{artistId}")
    public ResponseEntity<BaseResponse<?>> getPerformanceByArtist(
            @UserId(require = false) Long userId,
            @PathVariable(name="artistId") String artistId,
            @RequestParam(name="cursor", required = false) Long cursor
    ){
        PerformanceByArtistDTO performances = performanceFacade.getPerformanceByArtistId(userId, artistId, cursor);
        return ApiResponseUtil.success(SuccessMessage.SUCCESS, PerformanceByArtistResponse.of(performances, s3FileHandler));
    }

    @Permission(role = {Role.GENERAL})
    @GetMapping("/info")
    public ResponseEntity<BaseResponse<?>> getRecentPerformances(
            @UserId(require = false) Long userId
    ) {
        RecentPerformancesDTO recentPerformances = performanceFacade.getRecentPerformances(userId);
        return ApiResponseUtil.success(SuccessMessage.SUCCESS, RecentPerformancesResponse.of(recentPerformances, s3FileHandler));
    }
}
