package org.sopt.confeti.api.setlist.controller;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.sopt.confeti.domain.setlist.application.SetlistEditService;
import org.sopt.confeti.domain.setlist.application.dto.request.SetlistMusicOrderUpdateRequest;
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

    private final SetlistEditService setlistEditService;

    @PostMapping("/{setlistId}/edit/start")
    public ResponseEntity<BaseResponse<?>> startEdit(
            @UserId(require = false) Long userId,
            @PathVariable Long setlistId
    ) {
        setlistEditService.startEdit(userId, setlistId);
        return ApiResponseUtil.success(SuccessMessage.CREATED);
    }

    @PatchMapping("/{setlistId}/edit/musics/order")
    public ResponseEntity<BaseResponse<?>> updateMusicOrder(
            @UserId(require = false) Long userId,
            @PathVariable Long setlistId,
            @RequestBody List<SetlistMusicOrderUpdateRequest> request
    ) {
        setlistEditService.updateMusicOrder(userId, setlistId, request);
        return ApiResponseUtil.success(SuccessMessage.SUCCESS);
    }

    @DeleteMapping("/{setlistId}/musics/{orders}")
    public ResponseEntity<BaseResponse<?>> deleteMusic(
            @UserId(require = false) Long userId,
            @PathVariable Long setlistId,
            @PathVariable int orders
    ) {
        String deleteTrackId = setlistEditService.deleteMusic(userId, setlistId, orders);
        return ApiResponseUtil.success(SuccessMessage.DELETED, deleteTrackId);
    }

    @PatchMapping("/{setlistId}/edit/complete")
    public ResponseEntity<BaseResponse<?>> completeEdit(
            @UserId(require = false) Long userId,
            @PathVariable Long setlistId
    ) {
        setlistEditService.completeEdit(userId, setlistId);
        return ApiResponseUtil.success(SuccessMessage.UPDATED);
    }
}
