package org.sopt.confeti.api.setlist.controller;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.sopt.confeti.api.setlist.facade.SetlistFacade;
import org.sopt.confeti.api.setlist.facade.dto.request.SetlistUpdateMusicOrderDTO;
import org.sopt.confeti.global.annotation.UserId;
import org.sopt.confeti.global.common.BaseResponse;
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

    @PostMapping("/{setlistId}/edit/start")
    public ResponseEntity<BaseResponse<Void>> startEdit(
        @UserId Long userId,
        @PathVariable Long setlistId
    ) {
        setlistFacade.startEdit(userId, setlistId);
        return ApiResponseUtil.success(SuccessMessage.CREATED);
    }

    @PatchMapping("/{setlistId}/edit/musics/order")
    public ResponseEntity<BaseResponse<Void>> updateMusicOrder(
        @UserId Long userId,
        @PathVariable Long setlistId,
        @RequestBody List<SetlistUpdateMusicOrderDTO> request
    ) {
        setlistFacade.updateMusicOrder(userId, setlistId, request);
        return ApiResponseUtil.success(SuccessMessage.SUCCESS);
    }

    @DeleteMapping("/{setlistId}/musics/{orders}")
    public ResponseEntity<BaseResponse<String>> deleteMusic(
        @UserId Long userId,
        @PathVariable Long setlistId,
        @PathVariable int orders
    ) {
        String deleteTrackId = setlistFacade.deleteMusic(userId, setlistId, orders);
        return ApiResponseUtil.success(SuccessMessage.DELETED, deleteTrackId);
    }

    @PatchMapping("/{setlistId}/edit/complete")
    public ResponseEntity<BaseResponse<Void>> completeEdit(
        @UserId Long userId,
        @PathVariable Long setlistId
    ) {
        setlistFacade.completeEdit(userId, setlistId);
        return ApiResponseUtil.success(SuccessMessage.UPDATED);
    }

    @DeleteMapping("/{setlistId}/edit/cancel")
    public ResponseEntity<BaseResponse<Void>> cancelEdit(
        @UserId Long userId,
        @PathVariable Long setlistId
    ) {
        setlistFacade.cancelEdit(userId, setlistId);
        return ApiResponseUtil.success(SuccessMessage.SUCCESS);
    }
}
