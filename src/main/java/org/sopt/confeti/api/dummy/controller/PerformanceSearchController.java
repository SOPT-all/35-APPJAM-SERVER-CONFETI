package org.sopt.confeti.api.dummy.controller;

import lombok.RequiredArgsConstructor;
import org.sopt.confeti.api.dummy.facade.PerformanceSearchFacade;
import org.sopt.confeti.domain.user.constant.Role;
import org.sopt.confeti.global.annotation.Permission;
import org.sopt.confeti.global.common.BaseResponse;
import org.sopt.confeti.global.message.SuccessMessage;
import org.sopt.confeti.global.util.ApiResponseUtil;
import org.springframework.context.annotation.Profile;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("${api.endpoints.es.base}")
@Profile(value = {"prod"})
public class PerformanceSearchController {

    private final PerformanceSearchFacade performanceSearchFacade;

    @Permission(role = {Role.ADMIN})
    @PatchMapping("${api.endpoints.es.batch}")
    public ResponseEntity<BaseResponse<Void>> batch(
    ) {
        performanceSearchFacade.batch();
        return ApiResponseUtil.success(SuccessMessage.SUCCESS);
    }
}
