package org.sopt.confeti.api.admin.controller;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import org.sopt.confeti.api.admin.controller.docs.AdminControllerDocs;
import org.sopt.confeti.api.admin.dto.request.CreatePerformanceDraftRequest;
import org.sopt.confeti.api.admin.dto.request.CreateTicketVendorRequest;
import org.sopt.confeti.api.admin.dto.request.UpdateTicketVendorRequest;
import org.sopt.confeti.api.admin.dto.response.AdminArtistSearchResponses;
import org.sopt.confeti.api.admin.dto.response.AdminConcertDetailResponse;
import org.sopt.confeti.api.admin.dto.response.AdminConcertListResponse;
import org.sopt.confeti.api.admin.dto.response.AdminFestivalDetailResponse;
import org.sopt.confeti.api.admin.dto.response.AdminFestivalListResponse;
import org.sopt.confeti.api.admin.dto.response.PerformanceDraftResponse;
import org.sopt.confeti.api.admin.dto.response.TicketVendorResponse;
import org.sopt.confeti.api.admin.dto.response.TicketVendorResponses;
import org.sopt.confeti.api.admin.facade.AdminFacade;
import org.sopt.confeti.api.admin.facade.dto.response.AdminConcertDetailInfo;
import org.sopt.confeti.api.admin.facade.dto.response.AdminConcertListInfo;
import org.sopt.confeti.api.admin.facade.dto.response.AdminFestivalDetailInfo;
import org.sopt.confeti.api.admin.facade.dto.response.AdminFestivalListInfo;
import org.sopt.confeti.domain.performancedraft.application.dto.response.PerformanceDraftDto;
import org.sopt.confeti.global.annotation.Admin;
import org.sopt.confeti.global.common.BaseResponse;
import org.sopt.confeti.global.common.constant.RequestConstraint;
import org.sopt.confeti.global.message.SuccessMessage;
import org.sopt.confeti.global.util.ApiResponseUtil;
import org.sopt.confeti.global.util.S3FileHandler;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Admin
@RestController
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminController implements AdminControllerDocs {

    private final AdminFacade adminFacade;
    private final S3FileHandler s3FileHandler;

    @Override
    @PostMapping("/ticket-vendors")
    public ResponseEntity<BaseResponse<TicketVendorResponse>> createTicketVendor(
        @ModelAttribute CreateTicketVendorRequest request
    ) {
        return ApiResponseUtil.success(
            SuccessMessage.CREATED,
            adminFacade.createTicketVendor(request)
        );
    }

    @Override
    @PatchMapping("/ticket-vendors/{ticketVendorId}")
    public ResponseEntity<BaseResponse<TicketVendorResponse>> updateTicketVendor(
        @PathVariable Long ticketVendorId,
        @ModelAttribute UpdateTicketVendorRequest request
    ) {
        return ApiResponseUtil.success(
            SuccessMessage.UPDATED,
            adminFacade.updateTicketVendor(ticketVendorId, request)
        );
    }

    @Override
    @DeleteMapping("/ticket-vendors/{ticketVendorId}")
    public ResponseEntity<BaseResponse<Void>> deleteTicketVendor(
        @PathVariable Long ticketVendorId
    ) {
        adminFacade.deleteTicketVendor(ticketVendorId);
        return ApiResponseUtil.success(SuccessMessage.DELETED);
    }

    @Override
    @GetMapping("/ticket-vendors")
    public ResponseEntity<BaseResponse<TicketVendorResponses>> getTicketVendors() {
        return ApiResponseUtil.success(
            SuccessMessage.SUCCESS,
            TicketVendorResponses.from(adminFacade.getTicketVendors(), s3FileHandler)
        );
    }

    @Override
    @PostMapping("/performances/drafts")
    public ResponseEntity<BaseResponse<PerformanceDraftResponse>> createPerformanceDraft(
        @ModelAttribute CreatePerformanceDraftRequest request
    ) {
        PerformanceDraftDto performanceDraftDto = adminFacade.createPerformanceDraft(request.toCreateManualDto());
        return ApiResponseUtil.success(
            SuccessMessage.CREATED,
            PerformanceDraftResponse.from(performanceDraftDto, s3FileHandler)
        );  
    }

    @Override
    @GetMapping("/performances/concerts")
    public ResponseEntity<BaseResponse<AdminConcertListResponse>> getAdminConcerts() {
        AdminConcertListInfo concertList = adminFacade.getAdminConcerts();
        return ApiResponseUtil.success(SuccessMessage.SUCCESS,
            AdminConcertListResponse.of(concertList, s3FileHandler));
    }

    @Override
    @GetMapping("/performances/concerts/{concertId}")
    public ResponseEntity<BaseResponse<AdminConcertDetailResponse>> getAdminConcertDetail(
        @PathVariable("concertId") @Min(RequestConstraint.ID) long concertId
    ) {
        AdminConcertDetailInfo concertDetail = adminFacade.getAdminConcertDetail(concertId);
        return ApiResponseUtil.success(SuccessMessage.SUCCESS,
            AdminConcertDetailResponse.of(concertDetail, s3FileHandler));
    }

    @Override
    @GetMapping("/performances/festivals/{festivalId}")
    public ResponseEntity<BaseResponse<AdminFestivalDetailResponse>> getAdminFestivalDetail(
        @PathVariable("festivalId") @Min(RequestConstraint.ID) long festivalId
    ) {
        AdminFestivalDetailInfo festivalDetail = adminFacade.getAdminFestivalDetail(festivalId);
        return ApiResponseUtil.success(SuccessMessage.SUCCESS,
            AdminFestivalDetailResponse.of(festivalDetail, s3FileHandler));
    }

    @Override
    @GetMapping("/performances/festivals")
    public ResponseEntity<BaseResponse<AdminFestivalListResponse>> getAdminFestivals() {
        AdminFestivalListInfo festivalListInfo = adminFacade.getAdminFestivals();
        return ApiResponseUtil.success(SuccessMessage.SUCCESS,
            AdminFestivalListResponse.of(festivalListInfo, s3FileHandler));
    }

    @Override
    @GetMapping("/artists/search")
    public ResponseEntity<BaseResponse<AdminArtistSearchResponses>> searchArtists(
        @RequestParam @NotBlank @Size(max = 50) String term,
        @RequestParam(defaultValue = "5") @Min(1) @Max(25) int limit
    ) {
        return ApiResponseUtil.success(
            SuccessMessage.SUCCESS,
            adminFacade.searchArtists(term, limit)
        );
    }
}
