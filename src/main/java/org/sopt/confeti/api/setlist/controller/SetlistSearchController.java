package org.sopt.confeti.api.setlist.controller;

import lombok.RequiredArgsConstructor;
import org.sopt.confeti.api.setlist.dto.response.SetlistSearchPerformancesResponse;
import org.sopt.confeti.api.setlist.facade.SetlistFacade;
import org.sopt.confeti.api.setlist.facade.dto.response.SearchPerformancesDTO;
import org.sopt.confeti.domain.user.constant.Role;
import org.sopt.confeti.global.annotation.Permission;
import org.sopt.confeti.global.annotation.UserId;
import org.sopt.confeti.global.common.BaseResponse;
import org.sopt.confeti.global.message.SuccessMessage;
import org.sopt.confeti.global.util.ApiResponseUtil;
import org.sopt.confeti.global.util.S3FileHandler;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/my/setlists")
public class SetlistSearchController {

    private final SetlistFacade setlistFacade;
    private final S3FileHandler s3FileHandler;

    @Permission(role = {Role.GENERAL})
    @GetMapping("/performances/search")
    public ResponseEntity<BaseResponse<?>> searchPerformances(
            @UserId(require = false) Long userId,
            @RequestParam(required = false) String aid,
            @RequestParam(required = false) Long pid,
            @RequestParam(required = false) String term
    ) {
        SearchPerformancesDTO searchResult = setlistFacade.searchPerformances(aid, pid, term);
        return ApiResponseUtil.success(SuccessMessage.SUCCESS,
                SetlistSearchPerformancesResponse.of(searchResult, s3FileHandler));
    }
}
