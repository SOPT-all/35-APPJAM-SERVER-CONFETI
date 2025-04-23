package org.sopt.confeti.api.setlist;

import lombok.RequiredArgsConstructor;
import org.sopt.confeti.domain.setlist.SetlistSortType;
import org.sopt.confeti.domain.setlist.application.SetlistService;
import org.sopt.confeti.domain.setlist.application.dto.response.GetAllSetlistsResponse;
import org.sopt.confeti.global.common.BaseResponse;
import org.sopt.confeti.global.message.SuccessMessage;
import org.sopt.confeti.global.util.ApiResponseUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/my/setlists")
public class SetlistController {

    private final SetlistService setlistService;

    @GetMapping("/all")
    public ResponseEntity<BaseResponse<?>> getAllMySetlists(
            @AuthenticationPrincipal Long userId,
            @RequestParam(required = false) String sort
    ) {
        SetlistSortType sortType = SetlistSortType.from(sort);
        GetAllSetlistsResponse data = setlistService.getAllMySetlists(userId, sortType);
        return ApiResponseUtil.success(SuccessMessage.SUCCESS, data);
    }
}
