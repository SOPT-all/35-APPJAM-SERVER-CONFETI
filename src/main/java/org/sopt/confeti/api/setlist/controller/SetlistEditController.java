package org.sopt.confeti.api.setlist.controller;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.sopt.confeti.api.setlist.facade.SetlistFacade;
import org.sopt.confeti.api.setlist.facade.dto.request.SetlistUpdateSongOrderDTO;
import org.sopt.confeti.domain.user.constant.Role;
import org.sopt.confeti.global.annotation.Permission;
import org.sopt.confeti.global.common.BaseResponse;
import org.sopt.confeti.global.interceptor.auth.UserContext;
import org.sopt.confeti.global.message.SuccessMessage;
import org.sopt.confeti.global.util.ApiResponseUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/my/setlists")
public class SetlistEditController {

    private final SetlistFacade setlistFacade;

    @Permission(role = {Role.GENERAL})
    @PostMapping("/{setlistId}/edit/start")
    public ResponseEntity<BaseResponse<Void>> startEdit(
        @PathVariable Long setlistId
    ) {
        setlistFacade.startEdit(setlistId);
        return ApiResponseUtil.success(SuccessMessage.CREATED);
    }

    @Permission(role = {Role.GENERAL})
    @Deprecated
    @PatchMapping("/{setlistId}/edit/musics/order")
    public ResponseEntity<BaseResponse<Void>> updateMusicOrder(
        @PathVariable Long setlistId,
        @RequestBody List<SetlistUpdateSongOrderDTO> request
    ) {
        setlistFacade.updateSongOrder_deprecated(UserContext.get().id(), setlistId, request);
        return ApiResponseUtil.success(SuccessMessage.SUCCESS);
    }

    @Permission(role = {Role.GENERAL})
    @PatchMapping("/{setlistId}/edit/songs/order")
    public ResponseEntity<BaseResponse<Void>> updateSongOrder(
        @PathVariable Long setlistId,
        @RequestBody List<SetlistUpdateSongOrderDTO> request
    ) {
        setlistFacade.updateSongOrder(setlistId, request);
        return ApiResponseUtil.success(SuccessMessage.SUCCESS);
    }

    @Permission(role = {Role.GENERAL})
    @Deprecated
    @DeleteMapping("/{setlistId}/musics/{orders}")
    public ResponseEntity<BaseResponse<String>> deleteMusic(
        @PathVariable Long setlistId,
        @PathVariable int orders
    ) {
        String deleteTrackId = setlistFacade.deleteSong_deprecated(UserContext.get().id(),
            setlistId, orders);
        return ApiResponseUtil.success(SuccessMessage.DELETED, deleteTrackId);
    }

    @Permission(role = {Role.GENERAL})
    @DeleteMapping("/{setlistId}/songs/{orders}")
    public ResponseEntity<BaseResponse<String>> deleteSong(
        @PathVariable Long setlistId,
        @PathVariable int orders
    ) {
        String deleteTrackId = setlistFacade.deleteSong(setlistId, orders);
        return ApiResponseUtil.success(SuccessMessage.DELETED, deleteTrackId);
    }

    @Permission(role = {Role.GENERAL})
    @PatchMapping("/{setlistId}/edit/complete")
    public ResponseEntity<BaseResponse<Void>> completeEdit(
        @PathVariable Long setlistId
    ) {
        setlistFacade.completeEdit(setlistId);
        return ApiResponseUtil.success(SuccessMessage.UPDATED);
    }

    @Permission(role = {Role.GENERAL})
    @DeleteMapping("/{setlistId}/edit/cancel")
    public ResponseEntity<BaseResponse<Void>> cancelEdit(
        @PathVariable Long setlistId
    ) {
        setlistFacade.cancelEdit(setlistId);
        return ApiResponseUtil.success(SuccessMessage.SUCCESS);
    }
}
