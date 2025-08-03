package org.sopt.confeti.api.search.dto.response;

import java.util.List;
import java.util.Objects;
import org.sopt.confeti.api.search.facade.dto.response.SearchResultDTO;
import org.sopt.confeti.global.util.S3FileHandler;

public record SearchResultResponse(
        SearchResultArtistResponse artist,
        int performanceCount,
        List<SearchResultPerformanceResponse> performances
) {
    public static SearchResultResponse from(SearchResultDTO searchResult) {
        SearchResultArtistResponse artist = null;
        if (Objects.nonNull(searchResult.artist())) {
            artist = SearchResultArtistResponse.from(searchResult.artist());
        }

        return new SearchResultResponse(
                artist,
                searchResult.performances().size(),
                searchResult.performances().stream()
                        .map(SearchResultPerformanceResponse::from)
                        .toList()
        );
    }
}
