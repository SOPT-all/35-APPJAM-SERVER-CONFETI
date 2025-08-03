package org.sopt.confeti.api.search.facade.dto.response;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import org.sopt.confeti.domain.performance.Performance;
import org.sopt.confeti.domain.view.performance.application.dto.response.PerformanceDTO;
import org.sopt.confeti.global.resolver.music_api.artist.vo.ConfetiArtist;
import org.sopt.confeti.global.util.S3FileHandler;

public record SearchResultDTO(
        SearchResultArtistDTO artist,
        List<SearchResultPerformanceDTO> performances
) {
    public static SearchResultDTO of(ConfetiArtist artist, boolean artistFavorite, List<Performance> performances,
                                     Set<Long> favoritePerformanceIds, S3FileHandler s3FileHandler) {
        // null 일 수가 있나? -> Facade 계층에서 이미 NOT FOUND 예외를 던지고 있음
        // TODO: 민하한테 물어보기, 서버 전체 응답에 null, NOT FOUND 처리 정하기
        SearchResultArtistDTO artistDTO = null;
        if (Objects.nonNull(artist)) {
            artistDTO = SearchResultArtistDTO.of(artist, artistFavorite);
        }

        return new SearchResultDTO(
                artistDTO,
                performances.stream()
                        .map(performance -> SearchResultPerformanceDTO.of(
                                    performance,
                                    favoritePerformanceIds.contains(performance.getId()),
                                    s3FileHandler
                                )
                        )
                        .toList()
        );
    }

    public static SearchResultDTO of(PerformanceDTO performance, boolean performanceFavorite) {
        return new SearchResultDTO(
                null,
                List.of(SearchResultPerformanceDTO.of(performance, performanceFavorite))
        );
    }
}
