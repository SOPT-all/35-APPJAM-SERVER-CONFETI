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
import org.sopt.confeti.api.admin.dto.request.PutAdminConcertRequest;
import org.sopt.confeti.api.admin.dto.request.PutAdminFestivalRequest;
import org.sopt.confeti.api.admin.dto.request.UpdateTicketVendorRequest;
import org.sopt.confeti.api.admin.dto.response.AdminArtistSearchResponses;
import org.sopt.confeti.api.admin.dto.response.AdminConcertDetailResponse;
import org.sopt.confeti.api.admin.dto.response.AdminConcertListResponse;
import org.sopt.confeti.api.admin.dto.response.AdminFestivalDetailResponse;
import org.sopt.confeti.api.admin.dto.response.AdminFestivalListResponse;
import org.sopt.confeti.api.admin.dto.response.PerformanceDraftDetailResponse;
import org.sopt.confeti.api.admin.dto.response.PerformanceDraftListResponses;
import org.sopt.confeti.api.admin.dto.response.PerformanceDraftResponse;
import org.sopt.confeti.api.admin.dto.response.PutAdminConcertResponse;
import org.sopt.confeti.api.admin.dto.response.PutAdminFestivalResponse;
import org.sopt.confeti.api.admin.dto.response.TicketVendorResponse;
import org.sopt.confeti.api.admin.dto.response.TicketVendorResponses;
import org.sopt.confeti.domain.performancedraft.application.dto.response.PerformanceDraftDto;
import org.sopt.confeti.global.common.BaseResponse;
import org.sopt.confeti.global.common.constant.RequestConstraint;
import org.sopt.confeti.global.common.swagger.AuthErrorResponses;
import org.sopt.confeti.global.common.swagger.CommonErrorResponses;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

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


    @Operation(summary = "대기 공연 목록 조회", description = 
    """
    QuertString - search: 생략 가능. title, area를 기준으로 검색
    크롤링한 데이터의 경우 status가 "검토 필요" 반환되고,  
    수동 등록한 데이터의 경우 status가 "보류"로 반환됩니다. (현재 수동 등록의 경우 콘서트, 페스티벌 수정/저장 API를 사용하므로 해당 케이스는 존재하지 않습니다.)
            """)
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
    ResponseEntity<BaseResponse<PerformanceDraftListResponses>> getPerformanceDrafts(
        @RequestParam(required = false) @Size(max = 50) String search
    );

    @Deprecated
    @Operation(summary = "대기 공연 등록",
        description = "현재 해당 API를 사용하지 않습니다. 공연 수동 등록 시 페스티벌, 콘서트 각각의 수정/저장 API를 사용해야합니다. ")
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

    @Operation(summary = "대기 공연 상세 조회",
        description = """
        performanceData 는 아래와 같습니다. 
        festival인 경우
{
  "title": "2026 서울재즈페스티벌",
  "subtitle": "Seoul Jazz Festival",
  "startAt": "2026-05-22T00:00:00",
  "endAt": "2026-05-24T23:59:59",
  "area": "SEOUL",
  "reserveAt": "2026-03-20T12:00:00",
  "ageRating": "전체 관람가",
  "time": "12:00 ~ 22:00",
  "price": "200000",
  "address": "올림픽 공원",
  "timetableSupportStatus": "SUPPORTED",
  "reservationUrls": [
    {
      "name": "멜론 티켓",
      "url": "https://ticket.melon.com/performance/12345"
    },
    {
      "name": "인터파크 티켓",
      "url": "https://tickets.interpark.com/goods/12345"
    }
  ],
  "dates": [
    {
      "festivalAt": "2026-05-22",
      "openAt": "11:00:00",
      "dailyArtists": [
        {
          "artistId": "1163087245"
        },
        {
          "artistId": "1188975595"
        }
      ],
      "stages": [
        {
          "name": "May Forest Stage",
          "order": 1,
          "times": [
            {
              "startAt": "13:00:00",
              "endAt": "14:00:00",
              "artistId": "1163087245"
            },
            {
              "startAt": "14:30:00",
              "endAt": "15:30:00",
              "artistId": "1188975595"
            }
          ]
        },
        {
          "name": "Sparkling Dome",
          "order": 2,
          "times": []
        }
      ]
    },
    {
      "festivalAt": "2026-05-23",
      "openAt": "11:00:00",
      "dailyArtists": [
        {
          "artistId": "1203816887"
        },
        {
          "artistId": "1214153999"
        }
      ],
      "stages": [] 
    }
  ]
}

콘서트인 경우

{
  "title": "2026 카더가든 콘서트",
  "subtitle": "The Golden Hour",
  "startAt": "2026-04-01T18:00:00",
  "endAt": "2026-04-01T20:30:00",
  "area": "서울",
  "reserveAt": "2026-03-15T20:00:00",
  "ageRating": "만 7세 이상",
  "time": "150분",
  "price": "150000",
  "address": "올림픽 주경기장",
  "artists": [
    {
      "artistId": "1020577647"
    }
  ],
  "reservationUrls": [
    {
      "name": "인터파크 티켓",
      "reservationUrl": "https://ticket.interpark.com"
    }
  ]
}


"""
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
    ResponseEntity<BaseResponse<PerformanceDraftDetailResponse>> getPerformanceDraftDetail(
        @PathVariable Long draftId
    );

    @Deprecated
    @Operation(summary = "대기 공연 수정",
        description = """
        현재 해당 API를 사용하지 않습니다. 대기 공연 수정은 현재 요구사항에서 지원되지 않습니다.
        수정을 원하는 필드만 전달하면 해당 부분만 수정됩니다. 이미지는 선택적으로 교체할 수 있습니다.
        """
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

    @Operation(summary = "대기 공연 삭제")
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
    ResponseEntity<BaseResponse<Void>> deletePerformanceDraft(
        @PathVariable Long draftId
    );

    @Operation(
        summary = "등록된 콘서트 목록 조회",
        description = "어드민 권한으로 등록된 모든 콘서트 목록을 조회합니다. "
            + "진행 예정/진행 중인 콘서트와 종료된 콘서트를 구분하여 반환합니다."
            + "QuertString - search: 생략 가능. title, area를 기준으로 검색합니다."
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
    ResponseEntity<BaseResponse<AdminConcertListResponse>> getAdminConcerts(
        @RequestParam(required = false) @Size(max = 50) String search
    );

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
            + "QuertString - search: 생략 가능. title, area를 기준으로 검색합니다."
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
    ResponseEntity<BaseResponse<AdminFestivalListResponse>> getAdminFestivals(
        @RequestParam(required = false) @Size(max = 50) String search
    );

    @Operation(
        summary = "콘서트 등록/수정",
        description = "콘서트를 등록하거나 수정합니다. "
            + "concertId가 null이면 신규 등록, 값이 있으면 기존 콘서트를 수정합니다. "
            + "포스터 이미지는 multipart/form-data의 'posterFile' 파트로, "
            + "나머지 데이터는 'request' 파트(application/json)로 전송합니다."
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
    ResponseEntity<BaseResponse<PutAdminConcertResponse>> upsertConcert(
        @RequestPart MultipartFile poster,
        @Valid @RequestPart(value = "concert") PutAdminConcertRequest request
    );

    @Operation(
        summary = "페스티벌 등록/수정",
        description = "페스티벌을 등록하거나 수정합니다. "
            + "festivalId가 null이면 신규 등록, 값이 있으면 기존 페스티벌을 수정합니다. "
            + "포스터 이미지는 'poster' 파트, 로고 이미지는 'logo' 파트로, "
            + "나머지 데이터는 'festival' 파트(application/json)로 전송합니다."
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
    ResponseEntity<BaseResponse<PutAdminFestivalResponse>> upsertFestival(
        @RequestPart(required = false) MultipartFile poster,
        @RequestPart(required = false) MultipartFile logo,
        @Valid @RequestPart(value = "festival") PutAdminFestivalRequest request
    );

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
