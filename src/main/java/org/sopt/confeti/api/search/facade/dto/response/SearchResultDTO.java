package org.sopt.confeti.api.search.facade.dto.response;

import java.util.List;
import java.util.Map;
import org.sopt.confeti.domain.view.performance.application.dto.response.PerformanceDTO;
import org.sopt.confeti.global.resolver.music_api.artist.vo.ConfetiArtist;

public record SearchResultDTO(
        SearchResultArtistDTO artist,
        List<SearchResultPerformanceDTO> performances
) {
    public static SearchResultDTO of(ConfetiArtist artist, boolean artistFavorite, List<PerformanceDTO> performances,
                                     Map<Long, Boolean> performanceFavorites) {
        return new SearchResultDTO(
                SearchResultArtistDTO.of(artist, artistFavorite),
                performances.stream()
                        .map(performance -> SearchResultPerformanceDTO.of(performance,
                                performanceFavorites.get(performance.id())))
                        .toList()
        );
    }
}
