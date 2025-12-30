package org.sopt.confeti.api.search.controller;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.sopt.confeti.api.search.dto.response.PopularTermsResponse;
import org.sopt.confeti.api.search.dto.response.SearchResultResponse;
import org.sopt.confeti.api.search.facade.SearchFacade;
import org.sopt.confeti.api.search.facade.dto.response.PopularTermsDTO;
import org.sopt.confeti.api.search.facade.dto.response.SearchResultDTO;
import org.sopt.confeti.global.common.BaseResponse;
import org.sopt.confeti.global.message.SuccessMessage;
import org.sopt.confeti.global.util.ApiResponseUtil;
import org.sopt.confeti.global.util.S3FileHandler;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/search")
public class SearchController {

    private final SearchFacade searchFacade;
    private final S3FileHandler s3FileHandler;

    @GetMapping
    public ResponseEntity<BaseResponse<SearchResultResponse>> homeSearch(
        @RequestParam(required = false) String aid,
        @RequestParam(required = false) Long pid,
        @RequestParam(required = false) String term
    ) {
        SearchType searchType = SearchType.resolve(aid, pid, term);
        SearchResultDTO searchResult = searchType.search(searchFacade, aid, pid, term);

        return ApiResponseUtil.success(SuccessMessage.SUCCESS,
            SearchResultResponse.of(searchResult, s3FileHandler));
    }

    @GetMapping("/terms/popular")
    public ResponseEntity<BaseResponse<PopularTermsResponse>> getPopularSearchTerms(
        @RequestParam(required = false, defaultValue = "10") @Min(1) @Max(20) Integer limit
    ) {
        PopularTermsDTO terms = searchFacade.getPopularSearchTerms(limit);
        return ApiResponseUtil.success(SuccessMessage.SUCCESS, PopularTermsResponse.from(terms));
    }
}
