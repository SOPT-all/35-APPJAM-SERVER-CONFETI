package org.sopt.confeti.api.search.controller;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.sopt.confeti.api.search.dto.response.PopularTermsResponse;
import org.sopt.confeti.api.search.dto.response.SearchResultResponse;
import org.sopt.confeti.api.search.facade.SearchFacade;
import org.sopt.confeti.api.search.facade.dto.response.PopularTermsDTO;
import org.sopt.confeti.api.search.facade.dto.response.SearchResultDTO;
import org.sopt.confeti.domain.user.constant.Role;
import org.sopt.confeti.global.annotation.Permission;
import org.sopt.confeti.global.annotation.UserId;
import org.sopt.confeti.global.common.BaseResponse;
import org.sopt.confeti.global.exception.ConfetiException;
import org.sopt.confeti.global.message.ErrorMessage;
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
@RequestMapping("/search")
public class SearchController {

    private final SearchFacade searchFacade;
    private final S3FileHandler s3FileHandler;

    @Permission(role = {Role.GENERAL})
    @GetMapping
    public ResponseEntity<BaseResponse<?>> homeSearch(
            @UserId(require = false) Long userId,
            @RequestParam(required = false) String aid,
            @RequestParam(required = false) Long pid,
            @RequestParam(required = false) String term
    ) {
        if (Objects.isNull(aid) && Objects.isNull(pid) && Objects.isNull(term)) {
            throw new ConfetiException(ErrorMessage.BAD_REQUEST);
        }

        SearchResultDTO searchResult = null;

        if (Objects.nonNull(aid)) {
            searchResult = searchFacade.getHomeSearchResultWithAid(userId, aid);
        }

        return ApiResponseUtil.success(SuccessMessage.SUCCESS, SearchResultResponse.of(searchResult, s3FileHandler));
    }

    @Permission(role = {Role.GENERAL})
    @GetMapping("/terms/popular")
    public ResponseEntity<BaseResponse<?>> getPopularSearchTerms(
            @UserId(require = false) Long userId,
            @RequestParam(required = false, defaultValue = "10") @Min(1) @Max(20) Integer limit
    ) {
        PopularTermsDTO terms = searchFacade.getPopularSearchTerms(limit);
        return ApiResponseUtil.success(SuccessMessage.SUCCESS, PopularTermsResponse.from(terms));
    }
}
