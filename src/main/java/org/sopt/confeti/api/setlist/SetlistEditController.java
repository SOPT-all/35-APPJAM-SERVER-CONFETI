package org.sopt.confeti.api.setlist;

import lombok.RequiredArgsConstructor;
import org.sopt.confeti.domain.setlist.application.SetlistEditService;
import org.sopt.confeti.global.annotation.UserId;
import org.sopt.confeti.global.common.BaseResponse;
import org.sopt.confeti.global.message.SuccessMessage;
import org.sopt.confeti.global.util.ApiResponseUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
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
}
