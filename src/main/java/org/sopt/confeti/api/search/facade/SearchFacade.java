package org.sopt.confeti.api.search.facade;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.sopt.confeti.api.search.facade.dto.response.PopularTermsDTO;
import org.sopt.confeti.api.search.facade.dto.response.SearchResultDTO;
import org.sopt.confeti.domain.artist_favorite.application.ArtistFavoriteService;
import org.sopt.confeti.domain.elastic_search.application.SearchTermService;
import org.sopt.confeti.domain.view.performance.application.PerformanceService;
import org.sopt.confeti.domain.view.performance.application.dto.response.PerformanceDTO;
import org.sopt.confeti.global.annotation.Facade;
import org.sopt.confeti.global.common.constant.PerformanceType;
import org.sopt.confeti.global.exception.NotFoundException;
import org.sopt.confeti.global.message.ErrorMessage;
import org.sopt.confeti.global.resolver.music_api.artist.vo.ConfetiArtist;
import org.sopt.confeti.global.util.music.MusicAPIHandler;
import org.springframework.transaction.annotation.Transactional;

@Facade
@RequiredArgsConstructor
public class SearchFacade {

    private static final String TYPE_ALL = "ALL";

    private final SearchTermService searchTermService;
    private final MusicAPIHandler musicAPIHandler;
    private final ArtistFavoriteService artistFavoriteService;
    private final PerformanceService performanceService;

    @Transactional(readOnly = true)
    public SearchResultDTO getHomeSearchResultWithAid(Long userId, String aid) {
        ConfetiArtist artist = getArtistById(aid);
        boolean artistFavorite = false;

        if (Objects.nonNull(userId)) {
            artistFavorite = artistFavoriteService.isFavorite(userId, aid);
        }

        List<PerformanceDTO> performances = performanceService.getPerformancesByArtistIdAndType(aid,
                PerformanceType.PERFORMANCE);

        Map<Long, Boolean> performanceFavorites = performances.stream()
                .collect(Collectors.toMap(
                        PerformanceDTO::id,
                        p -> false
                ));

        if (Objects.nonNull(userId)) {
            List<PerformanceDTO> favoritePerformances = performanceService.getFavoritePerformancesAll(userId, TYPE_ALL);
            getPerformanceFavorites(performanceFavorites, favoritePerformances);
        }

        return SearchResultDTO.of(artist, artistFavorite, performances, performanceFavorites);
    }

    @Transactional(readOnly = true)
    public void getHomeSearchResultWithPid(Long userId, long pid) {
    }

    @Transactional(readOnly = true)
    public void getHomeSearchResultWithTerm(Long userId, String term) {
    }

    public PopularTermsDTO getPopularSearchTerms(int limit) {
        return PopularTermsDTO.from(searchTermService.getPopularSearchTerms(limit));
    }

    private ConfetiArtist getArtistById(String aid) {
        return musicAPIHandler.findArtistByArtistId(aid)
                .orElseThrow(
                        () -> new NotFoundException(ErrorMessage.NOT_FOUND)
                );
    }

    private void getPerformanceFavorites(Map<Long, Boolean> performanceFavorites,
                                         List<PerformanceDTO> favoritePerformances) {
        favoritePerformances.forEach(performance -> {
            performanceFavorites.replace(performance.id(), true);
        });
    }
}
