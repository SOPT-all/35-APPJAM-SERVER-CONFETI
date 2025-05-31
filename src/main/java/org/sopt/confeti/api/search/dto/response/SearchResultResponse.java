package org.sopt.confeti.api.search.dto.response;

import java.util.List;
import org.sopt.confeti.api.search.facade.dto.response.SearchResultDTO;
import org.sopt.confeti.global.util.S3FileHandler;

public record SearchResultResponse(
        SearchResultArtistResponse artist,
        int performanceCount,
        List<SearchResultPerformanceResponse> performances
) {
    public static SearchResultResponse of(SearchResultDTO searchResult, S3FileHandler s3FileHandler) {
        return new SearchResultResponse(
                SearchResultArtistResponse.from(searchResult.artist()),
                searchResult.performances().size(),
                searchResult.performances().stream()
                        .map(performance -> SearchResultPerformanceResponse.of(performance, s3FileHandler))
                        .toList()
        );
    }
}
