package org.sopt.confeti.api.setlist.facade;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.sopt.confeti.api.setlist.facade.dto.response.search.SearchedPerformancesDTO;
import org.sopt.confeti.api.setlist.facade.dto.response.search.SetlistSearchArtistMusicsDTO;
import org.sopt.confeti.api.setlist.facade.dto.response.search.SetlistSearchMusicsDTO;
import org.sopt.confeti.domain.elastic_search.application.PerformanceSearchService;
import org.sopt.confeti.domain.performance.PerformanceType;
import org.sopt.confeti.domain.performance.SearchedPerformance;
import org.sopt.confeti.domain.performance.application.PerformanceService;
import org.sopt.confeti.domain.view.performance.application.PerformanceService_DPRECATED;
import org.sopt.confeti.global.annotation.Facade;
import org.sopt.confeti.global.exception.ConfetiException;
import org.sopt.confeti.global.exception.NotFoundException;
import org.sopt.confeti.global.message.ErrorMessage;
import org.sopt.confeti.global.resolver.music_api.artist.vo.ConfetiArtist;
import org.sopt.confeti.global.util.S3FileHandler;
import org.sopt.confeti.global.util.analyzer.SearchTermAnalyzer;
import org.sopt.confeti.global.util.analyzer.dto.PerformanceSearchTermAnalyzeResult;
import org.sopt.confeti.global.util.music.MusicAPIHandler;
import org.sopt.confeti.global.util.music.dto.music.MusicPage;
import org.springframework.transaction.annotation.Transactional;

@Facade
@RequiredArgsConstructor
public class SetlistSearchFacade {

    private static final int SEARCH_ARTIST_BY_KEYWORD_LIMIT = 1;

    private final PerformanceService_DPRECATED performanceServiceDPRECATED;
    private final MusicAPIHandler musicAPIHandler;
    private final PerformanceSearchService performanceSearchService;
    private final PerformanceService performanceService;
    private final S3FileHandler s3FileHandler;

    private boolean isPresent(Object that) {
        return Objects.nonNull(that);
    }

    private boolean isNotPresent(Object that) {
        return Objects.isNull(that);
    }

    public SetlistSearchMusicsDTO searchMusics(String term, int offset, int limit) {
        return SetlistSearchMusicsDTO.from(
                musicAPIHandler.getMusicsByKeyword(term, offset, limit)
        );
    }

    public SetlistSearchArtistMusicsDTO searchArtistMusics(String aid, String term, int offset, int limit) {
        String artistId = null;

        if (isPresent(aid)) {
            artistId = aid;
        }

        if (isNotPresent(artistId)) {
            Optional<String> searchedArtistId = getArtistId(term);

            if (searchedArtistId.isEmpty()) {
                throw new ConfetiException(ErrorMessage.BAD_REQUEST);
            }

            artistId = searchedArtistId.get();
        }

        MusicPage musics = musicAPIHandler.getArtistMusicsByArtistId(artistId, offset, limit);
        return SetlistSearchArtistMusicsDTO.from(musics);
    }

    @Transactional(readOnly = true)
    public SearchedPerformancesDTO searchPerformances(String aid, Long pid, String term) {
        List<SearchedPerformance> performances = new ArrayList<>();
        PerformanceSearchTermAnalyzeResult analyzeResult = PerformanceSearchTermAnalyzeResult.empty();

        if (isPresent(aid)) {
            performances.addAll(
                    performanceService.getPerformancesByArtistId(aid).stream()
                            .map(performance -> SearchedPerformance.of(performance, s3FileHandler))
                            .toList()
            );
        }

        if (isPresent(pid)) {
            performances.add(
                    performanceService.getPerformance(pid)
                            .map(performance -> SearchedPerformance.of(performance, s3FileHandler))
                            .orElseThrow(() -> new NotFoundException(ErrorMessage.NOT_FOUND))
            );
        }

        if (isPresent(term)) {
            analyzeResult = SearchTermAnalyzer.analyzePerformance(term);
            Optional<String> artistId = getArtistId(analyzeResult.processedTerm());

            artistId.ifPresent(presentedAid -> performances.addAll(
                    performanceService.getPerformancesByArtistId(presentedAid).stream()
                            .map(performance -> SearchedPerformance.of(performance, s3FileHandler))
                            .toList()
            ));

            performances.addAll(
                    performanceSearchService.getPerformancesByTitleAndTypePartialMatched(analyzeResult.processedTerm(),
                                    analyzeResult.performanceType()).stream()
                            .map(searchResult -> SearchedPerformance.of(searchResult, s3FileHandler))
                            .toList()
            );
        }

        PerformanceSearchTermAnalyzeResult finalAnalyzeResult = analyzeResult;
        Set<SearchedPerformance> searchedPerformances = performances.stream()
                .filter(
                        searchedPerformance -> finalAnalyzeResult.performanceType() == PerformanceType.PERFORMANCE ||
                                searchedPerformance.type() == finalAnalyzeResult.performanceType()
                )
                .collect(Collectors.toSet());

        return SearchedPerformancesDTO.from(
                searchedPerformances.stream()
                        .sorted(Comparator.comparing(SearchedPerformance::startAt).reversed())
                        .toList()
        );
    }

    private Optional<String> getArtistId(String processedTerm) {
        return musicAPIHandler.findArtistsByKeyword(processedTerm, SEARCH_ARTIST_BY_KEYWORD_LIMIT).stream()
                .findFirst()
                .map(ConfetiArtist::getId);
    }
}
