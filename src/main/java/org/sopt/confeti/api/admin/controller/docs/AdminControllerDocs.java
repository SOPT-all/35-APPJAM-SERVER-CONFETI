package org.sopt.confeti.api.admin.controller.docs;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import org.sopt.confeti.api.admin.dto.request.CreatePerformanceDraftRequest;
import org.sopt.confeti.api.admin.dto.request.CreateTicketVendorRequest;
import org.sopt.confeti.api.admin.dto.request.UpdatePerformanceDraftRequest;
import org.sopt.confeti.api.admin.dto.request.UpdateTicketVendorRequest;
import org.sopt.confeti.api.admin.dto.response.AdminArtistSearchResponses;
import org.sopt.confeti.api.admin.dto.response.AdminConcertDetailResponse;
import org.sopt.confeti.api.admin.dto.response.AdminConcertListResponse;
import org.sopt.confeti.api.admin.dto.response.AdminFestivalDetailResponse;
import org.sopt.confeti.api.admin.dto.response.AdminFestivalListResponse;
import org.sopt.confeti.api.admin.dto.response.PerformanceDraftDetailResponse;
import org.sopt.confeti.api.admin.dto.response.PerformanceDraftListResponses;
import org.sopt.confeti.api.admin.dto.response.PerformanceDraftResponse;
import org.sopt.confeti.api.admin.dto.response.TicketVendorResponse;
import org.sopt.confeti.api.admin.dto.response.TicketVendorResponses;
import org.sopt.confeti.domain.performancedraft.application.dto.response.PerformanceDraftDto;
import org.sopt.confeti.global.common.BaseResponse;
import org.sopt.confeti.global.common.constant.RequestConstraint;
import org.sopt.confeti.global.common.swagger.AuthErrorResponses;
import org.sopt.confeti.global.common.swagger.CommonErrorResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PatchMapping;

@Tag(name = "어드민 API", description = "어드민 전용 API")
public interface AdminControllerDocs {

    @Operation(summary = "예매처 생성")
    @ApiResponses(
        value = {
            @ApiResponse(
                responseCode = "201",
                description = "생성 성공"
            )
        }
    )
    @AuthErrorResponses
    @CommonErrorResponses
    @PostMapping(value = "/ticket-vendors", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    ResponseEntity<BaseResponse<TicketVendorResponse>> createTicketVendor(
        @ModelAttribute @Valid CreateTicketVendorRequest request
    );

    @Operation(summary = "예매처 수정 (Patch)",
        description = "수정을 원하는 필드만 전달하면 해당 부분만 수정을 진행함"
    )
    @ApiResponses(
        value = {
            @ApiResponse(
                responseCode = "200",
                description = "성공"
            )
        }
    )
    @AuthErrorResponses
    @CommonErrorResponses
    @PatchMapping(value = "/ticket-vendors/{ticketVendorId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    ResponseEntity<BaseResponse<TicketVendorResponse>> updateTicketVendor(
        @PathVariable Long ticketVendorId,
        @ModelAttribute @Valid UpdateTicketVendorRequest request
    );

    @Operation(summary = "예매처 삭제")
    @ApiResponses(
        value = {
            @ApiResponse(
                responseCode = "200",
                description = "성공"
            )
        }
    )
    @AuthErrorResponses
    @CommonErrorResponses
    ResponseEntity<BaseResponse<Void>> deleteTicketVendor(
        @PathVariable Long ticketVendorId
    );

    @Operation(summary = "예매처 전체 조회")
    @ApiResponses(
        value = {
            @ApiResponse(
                responseCode = "200",
                description = "성공"
            )
        }
    )
    @AuthErrorResponses
    @CommonErrorResponses
    ResponseEntity<BaseResponse<TicketVendorResponses>> getTicketVendors();


    @Operation(summary = "대기 공연 목록 조회")
    @ApiResponses(
        value = {
            @ApiResponse(
                responseCode = "200",
                description = "성공"
            )
        }
    )
    @AuthErrorResponses
    @CommonErrorResponses
    ResponseEntity<BaseResponse<PerformanceDraftListResponses>> getPerformanceDrafts();

    @Operation(summary = "대기 공연 등록")
    @ApiResponses(
        value = {
            @ApiResponse(
                responseCode = "201",
                description = "성공"
            )
        }
    )
    @AuthErrorResponses
    @CommonErrorResponses
    public ResponseEntity<BaseResponse<PerformanceDraftResponse>> createPerformanceDraft(
        @ModelAttribute @Valid CreatePerformanceDraftRequest request
    );

    @Operation(summary = "대기 공연 상세 조회")
    @ApiResponses(
        value = {
            @ApiResponse(
                responseCode = "200",
                description = "성공"
            )
        }
    )
    @AuthErrorResponses
    @CommonErrorResponses
    ResponseEntity<BaseResponse<PerformanceDraftDetailResponse>> getPerformanceDraftDetail(
        @PathVariable Long draftId
    );

    @Operation(summary = "대기 공연 수정",
        description = "수정을 원하는 필드만 전달하면 해당 부분만 수정됩니다. 이미지는 선택적으로 교체할 수 있습니다."
    )
    @ApiResponses(
        value = {
            @ApiResponse(
                responseCode = "200",
                description = "성공"
            )
        }
    )
    @AuthErrorResponses
    @CommonErrorResponses
    @PatchMapping(value = "/performances/drafts/{draftId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    ResponseEntity<BaseResponse<PerformanceDraftResponse>> updatePerformanceDraft(
        @PathVariable Long draftId,
        @ModelAttribute UpdatePerformanceDraftRequest request
    );

    @Operation(
        summary = "등록된 콘서트 목록 조회",
        description = "어드민 권한으로 등록된 모든 콘서트 목록을 조회합니다. "
            + "진행 예정/진행 중인 콘서트와 종료된 콘서트를 구분하여 반환합니다."
    )
    @ApiResponses(
        value = {
            @ApiResponse(
                responseCode = "200",
                description = "성공"
            )
        }
    )
    @AuthErrorResponses
    @CommonErrorResponses
    ResponseEntity<BaseResponse<AdminConcertListResponse>> getAdminConcerts();

    @Operation(
        summary = "수정할 콘서트 단건 조회",
        description = "어드민 권한으로 수정할 콘서트의 전체 상세 정보를 조회합니다. "
            + "예정된 공연 여부와 관계없이 모든 콘서트를 조회할 수 있습니다."
    )
    @ApiResponses(
        value = {
            @ApiResponse(
                responseCode = "200",
                description = "성공"
            )
        }
    )
    @AuthErrorResponses
    @CommonErrorResponses
    ResponseEntity<BaseResponse<AdminConcertDetailResponse>> getAdminConcertDetail(
        @PathVariable("concertId") @Min(RequestConstraint.ID) long concertId
    );

    @Operation(
        summary = "수정할 페스티벌 단건 조회",
        description = "어드민 권한으로 수정할 페스티벌의 전체 상세 정보를 조회합니다. "
            + "예정된 공연 여부와 관계없이 모든 페스티벌을 조회할 수 있습니다."
    )
    @ApiResponses(
        value = {
            @ApiResponse(
                responseCode = "200",
                description = "성공"
            )
        }
    )
    @AuthErrorResponses
    @CommonErrorResponses
    ResponseEntity<BaseResponse<AdminFestivalDetailResponse>> getAdminFestivalDetail(
        @PathVariable("festivalId") @Min(RequestConstraint.ID) long festivalId
    );

    @Operation(
        summary = "등록된 페스티벌 목록 조회",
        description = "어드민 권한으로 등록된 모든 페스티벌 목록을 조회합니다. "
            + "진행 예정/진행 중인 페스티벌과 종료된 페스티벌을 구분하여 반환합니다."
    )
    @ApiResponses(
        value = {
            @ApiResponse(
                responseCode = "200",
                description = "성공"
            )
        }
    )
    @AuthErrorResponses
    @CommonErrorResponses
    ResponseEntity<BaseResponse<AdminFestivalListResponse>> getAdminFestivals();

    @Operation(summary = "아티스트 검색")
    @ApiResponses(
        value = {
            @ApiResponse(
                responseCode = "200",
                description = "성공"
            )
        }
    )
    @AuthErrorResponses
    @CommonErrorResponses
    ResponseEntity<BaseResponse<AdminArtistSearchResponses>> searchArtists(
        @RequestParam @NotBlank @Size(max = 50) String term,
        @RequestParam(defaultValue = "5") @Min(1) @Max(25) int limit
    );
}
