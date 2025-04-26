package org.sopt.confeti.api.search_term.controller;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.sopt.confeti.api.search_term.dto.response.PopularTermsResponse;
import org.sopt.confeti.api.search_term.facade.SearchTermFacade;
import org.sopt.confeti.api.search_term.facade.dto.response.PopularTermsDTO;
import org.sopt.confeti.domain.user.constant.Role;
import org.sopt.confeti.global.annotation.Permission;
import org.sopt.confeti.global.annotation.UserId;
import org.sopt.confeti.global.common.BaseResponse;
import org.sopt.confeti.global.message.SuccessMessage;
import org.sopt.confeti.global.util.ApiResponseUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/search/terms")
public class SearchTermController {

    private final SearchTermFacade searchTermFacade;

    @Permission(role = {Role.GENERAL})
    @GetMapping("/popular")
    public ResponseEntity<BaseResponse<?>> getPopularSearchTerms(
            @UserId(require = false) Long userId,
            @RequestParam(required = false, defaultValue = "10") @Min(1) @Max(20) Integer limit
    ) {
        PopularTermsDTO terms = searchTermFacade.getPopularSearchTerms(limit);
        return ApiResponseUtil.success(SuccessMessage.SUCCESS, PopularTermsResponse.from(terms));
    }
}
