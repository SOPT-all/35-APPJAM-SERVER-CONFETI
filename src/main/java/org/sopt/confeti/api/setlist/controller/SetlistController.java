package org.sopt.confeti.api.setlist.controller;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.sopt.confeti.domain.setlist.SetlistSortType;
import org.sopt.confeti.domain.setlist.application.SetlistService;
import org.sopt.confeti.domain.setlist.application.dto.request.AddSetListMusicRequest;
import org.sopt.confeti.domain.setlist.application.dto.request.SetlistCreateRequest;
import org.sopt.confeti.domain.setlist.application.dto.response.AddSetListMusicResponse;
import org.sopt.confeti.domain.setlist.application.dto.response.GetAllSetlistsResponse;
import org.sopt.confeti.domain.setlist.application.dto.response.GetSetlistDetailResponse;
import org.sopt.confeti.domain.setlist.application.dto.response.SetlistCreateResponse;
import org.sopt.confeti.domain.setlist.application.dto.response.SetlistSummaryDto;
import org.sopt.confeti.domain.user.constant.Role;
import org.sopt.confeti.global.annotation.Permission;
import org.sopt.confeti.global.annotation.UserId;
import org.sopt.confeti.global.common.BaseResponse;
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
public class SetlistController {

    private final SetlistService setlistService;

    @Permission(role = {Role.GENERAL})
    @GetMapping("/all")
    public ResponseEntity<BaseResponse<?>> getAllMySetlists(
            @UserId(require = false) Long userId,
            @RequestParam(name = "sortBy", required = false) String sortBy
    ) {
        SetlistSortType sortType = SetlistSortType.from(sortBy);
        GetAllSetlistsResponse data = setlistService.getAllMySetlists(userId, sortType);
        return ApiResponseUtil.success(SuccessMessage.SUCCESS, data);
    }

    @Permission(role = {Role.GENERAL})
    @GetMapping("/preview")
    public ResponseEntity<BaseResponse<?>> getPreviewMySetlists(
            @UserId(require = false) Long userId
    ) {
        List<SetlistSummaryDto> data = setlistService.getPreviewMySetlists(userId);
        return ApiResponseUtil.success(SuccessMessage.SUCCESS, data);
    }

    @Permission(role = {Role.GENERAL})
    @PostMapping
    public ResponseEntity<BaseResponse<?>> createSetlists(
            @UserId(require = false) Long userId,
            @RequestBody List<SetlistCreateRequest> requests
    ) {
        List<Long> ids = setlistService.createSetLists(userId, requests);
        return ApiResponseUtil.success(SuccessMessage.CREATED, new SetlistCreateResponse(ids));
    }

    @Permission(role = {Role.GENERAL})
    @PostMapping("/{setlistId}/musics")
    public ResponseEntity<BaseResponse<?>> addMusicsToSetlist(
            @UserId(require = false) Long userId,
            @PathVariable Long setlistId,
            @RequestBody List<AddSetListMusicRequest> requests
    ) {
        int count = setlistService.addMusics(userId, setlistId, requests);
        return ApiResponseUtil.success(SuccessMessage.CREATED, new AddSetListMusicResponse(count));
    }

    @Permission(role = {Role.GENERAL})
    @GetMapping("/{setlistId}")
    public ResponseEntity<BaseResponse<?>> getSetlistDetail(
            @UserId(require = false) Long userId,
            @PathVariable Long setlistId
    ) {
        GetSetlistDetailResponse data = setlistService.getSetlistDetail(userId, setlistId);
        return ApiResponseUtil.success(SuccessMessage.SUCCESS, data);
    }
}
