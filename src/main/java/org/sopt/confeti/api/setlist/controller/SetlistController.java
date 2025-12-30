package org.sopt.confeti.api.setlist.controller;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.sopt.confeti.api.setlist.controller.docs.SetlistControllerDocs;
import org.sopt.confeti.api.setlist.dto.response.GetAllSetlistsResponse;
import org.sopt.confeti.api.setlist.dto.response.GetSetlistDetailResponse;
import org.sopt.confeti.api.setlist.dto.response.GetSetlistDetailResponse_deprecated;
import org.sopt.confeti.api.setlist.dto.response.SetlistSummaryResponse;
import org.sopt.confeti.api.setlist.facade.SetlistFacade;
import org.sopt.confeti.api.setlist.facade.dto.request.SetlistAddSongDTO;
import org.sopt.confeti.api.setlist.facade.dto.request.SetlistCreateRequestDTO;
import org.sopt.confeti.api.setlist.facade.dto.response.SetlistAddSongResponseDTO;
import org.sopt.confeti.api.setlist.facade.dto.response.SetlistCreateResponseDTO;
import org.sopt.confeti.domain.setlist.SetlistSortType;
import org.sopt.confeti.domain.user.constant.Role;
import org.sopt.confeti.global.annotation.ApiVersion;
import org.sopt.confeti.global.annotation.Permission;
import org.sopt.confeti.global.common.BaseResponse;
import org.sopt.confeti.global.interceptor.auth.UserContext;
import org.sopt.confeti.global.message.SuccessMessage;
import org.sopt.confeti.global.util.ApiResponseUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/my/setlists")
public class SetlistController implements SetlistControllerDocs {

    private final SetlistFacade setlistFacade;

    @Permission(role = {Role.GENERAL})
    @GetMapping("/all")
    @Deprecated
    public ResponseEntity<BaseResponse<GetAllSetlistsResponse>> getAllMySetlists_deprecated(
        @RequestParam(name = "sortBy", required = false) String sortBy
    ) {
        GetAllSetlistsResponse data = setlistFacade.getAllMySetlists_deprecated(
            UserContext.get().id(), sortBy);
        return ApiResponseUtil.success(SuccessMessage.SUCCESS, data);
    }

    @ApiVersion("2")
    @Permission(role = {Role.GENERAL})
    @GetMapping("/all")
    public ResponseEntity<BaseResponse<GetAllSetlistsResponse>> getAllMySetlists(
        @RequestParam(required = false) SetlistSortType sortBy
    ) {
        GetAllSetlistsResponse data = setlistFacade.getAllMySetlists(sortBy);
        return ApiResponseUtil.success(SuccessMessage.SUCCESS, data);
    }

    @Permission(role = {Role.GENERAL})
    @GetMapping("/preview")
    @Deprecated
    public ResponseEntity<BaseResponse<List<SetlistSummaryResponse>>> getPreviewMySetlists_deprecated(
    ) {
        List<SetlistSummaryResponse> data = setlistFacade.getPreviewMySetlists_deprecated(
            UserContext.get().id());
        return ApiResponseUtil.success(SuccessMessage.SUCCESS, data);
    }

    @ApiVersion("2")
    @Permission(role = {Role.GENERAL})
    @GetMapping("/preview")
    public ResponseEntity<BaseResponse<List<SetlistSummaryResponse>>> getPreviewMySetlists() {
        List<SetlistSummaryResponse> data = setlistFacade.getPreviewMySetlists();
        return ApiResponseUtil.success(SuccessMessage.SUCCESS, data);
    }

    @Permission(role = {Role.GENERAL})
    @PostMapping
    public ResponseEntity<BaseResponse<SetlistCreateResponseDTO>> createSetlists(
        @RequestBody List<SetlistCreateRequestDTO> requests
    ) {
        SetlistCreateResponseDTO data = setlistFacade.createSetLists(requests);
        return ApiResponseUtil.success(SuccessMessage.CREATED, data);
    }

    @Permission(role = {Role.GENERAL})
    @PostMapping("/{setlistId}/musics")
    public ResponseEntity<BaseResponse<SetlistAddSongResponseDTO>> addMusicsToSetlist(
        @PathVariable Long setlistId,
        @RequestBody List<SetlistAddSongDTO> requests
    ) {
        SetlistAddSongResponseDTO data = setlistFacade.addSongs(setlistId, requests);
        return ApiResponseUtil.success(SuccessMessage.CREATED, data);
    }

    @Permission(role = {Role.GENERAL})
    @GetMapping("/{setlistId}")
    @Deprecated
    public ResponseEntity<BaseResponse<GetSetlistDetailResponse_deprecated>> getSetlistDetail_deprecated(
        @PathVariable Long setlistId
    ) {
        GetSetlistDetailResponse_deprecated data = setlistFacade.getSetlistDetail_deprecated(
            UserContext.get().id(),
            setlistId);
        return ApiResponseUtil.success(SuccessMessage.SUCCESS, data);
    }

    @ApiVersion("2")
    @Permission(role = {Role.GENERAL})
    @GetMapping("/{setlistId}")
    public ResponseEntity<BaseResponse<GetSetlistDetailResponse>> getSetlistDetail(
        @PathVariable Long setlistId
    ) {
        GetSetlistDetailResponse data = setlistFacade.getSetlistDetail(setlistId);
        return ApiResponseUtil.success(SuccessMessage.SUCCESS, data);
    }
}
