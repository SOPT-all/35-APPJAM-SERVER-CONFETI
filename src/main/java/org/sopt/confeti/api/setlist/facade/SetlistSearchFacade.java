package org.sopt.confeti.api.setlist.facade;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import kr.co.shineware.nlp.komoran.model.KomoranResult;
import lombok.RequiredArgsConstructor;
import org.sopt.confeti.api.setlist.facade.dto.response.search.SearchPerformancesDTO;
import org.sopt.confeti.api.setlist.facade.dto.response.search.SetlistSearchArtistMusicsDTO;
import org.sopt.confeti.domain.elastic_search.application.PerformanceSearchService;
import org.sopt.confeti.domain.view.performance.application.PerformanceService;
import org.sopt.confeti.domain.view.performance.application.dto.response.PerformanceDTO;
import org.sopt.confeti.global.annotation.Facade;
import org.sopt.confeti.global.common.AnalyzeSearchTermResult;
import org.sopt.confeti.global.common.constant.PerformanceType;
import org.sopt.confeti.global.exception.ConfetiException;
import org.sopt.confeti.global.message.ErrorMessage;
import org.sopt.confeti.global.resolver.music_api.artist.vo.ConfetiArtist;
import org.sopt.confeti.global.util.MorphemeAnalyzer;
import org.sopt.confeti.global.util.music.MusicAPIHandler;
import org.sopt.confeti.global.util.music.dto.music.MusicPage;
import org.springframework.transaction.annotation.Transactional;

@Facade
@RequiredArgsConstructor
public class SetlistSearchFacade {

    private static final int SEARCH_ARTIST_BY_KEYWORD_LIMIT = 1;

    private final PerformanceService performanceService;
    private final MusicAPIHandler musicAPIHandler;
    private final PerformanceSearchService performanceSearchService;

    private boolean isPresent(Object that) {
        return Objects.nonNull(that);
    }

    private boolean isNotPresent(Object that) {
        return Objects.isNull(that);
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

        MusicPage musics = musicAPIHandler.getArtistMusics(artistId, offset, limit);
        return SetlistSearchArtistMusicsDTO.from(musics);
    }

    @Transactional(readOnly = true)
    public SearchPerformancesDTO searchPerformances(String aid, Long pid, String term) {
        List<PerformanceDTO> performances = new ArrayList<>();
        AnalyzeSearchTermResult analyzeResult = AnalyzeSearchTermResult.empty();

        if (isPresent(aid)) {
            performances.addAll(performanceService.getAllPerformancesByArtistId(aid));
        }

        if (isPresent(pid)) {
            performances.add(PerformanceDTO.from(performanceService.getPerformanceById(pid)));
        }

        if (isPresent(term)) {
            analyzeResult = analyzeSearchTerm(term);
            Optional<String> artistId = getArtistId(analyzeResult.processedTerm());

            artistId.ifPresent(s -> performances.addAll(performanceService.getAllPerformancesByArtistId(s)));

            performances.addAll(
                    performanceSearchService.getPerformancesByTitleAndTypePartialMatched(analyzeResult.processedTerm(),
                                    analyzeResult.performanceType()).stream()
                            .map(PerformanceDTO::from)
                            .toList()
            );
        }

        AnalyzeSearchTermResult finalAnalyzeResult = analyzeResult;
        Set<PerformanceDTO> searchedPerformances = performances.stream()
                .filter(
                        performance -> finalAnalyzeResult.performanceType() == PerformanceType.PERFORMANCE ||
                                performance.type() == finalAnalyzeResult.performanceType()
                )
                .collect(Collectors.toSet());

        return SearchPerformancesDTO.from(
                searchedPerformances.stream()
                        .sorted(Comparator.comparing(PerformanceDTO::startAt).reversed())
                        .toList()
        );
    }

    private AnalyzeSearchTermResult analyzeSearchTerm(String term) {
        KomoranResult analyzeResult = MorphemeAnalyzer.getAnalyzeResult(term);
        String processedTerm = MorphemeAnalyzer.getRemovedPerformanceTypesTerm(term, analyzeResult);
        PerformanceType performanceType = MorphemeAnalyzer.getFirstMatchingPerformanceType(analyzeResult);

        return AnalyzeSearchTermResult.of(processedTerm, performanceType);
    }

    private Optional<String> getArtistId(String processedTerm) {
        return musicAPIHandler.findArtistsByKeyword(processedTerm, SEARCH_ARTIST_BY_KEYWORD_LIMIT).stream()
                .findFirst()
                .map(ConfetiArtist::getId);
    }
}
