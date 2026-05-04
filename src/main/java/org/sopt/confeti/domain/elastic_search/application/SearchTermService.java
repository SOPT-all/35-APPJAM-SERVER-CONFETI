package org.sopt.confeti.domain.elastic_search.application;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.sopt.confeti.domain.elastic_search.SearchTermDocument;
import org.sopt.confeti.domain.elastic_search.application.dto.response.PopularTermResult;
import org.sopt.confeti.domain.elastic_search.infra.SearchTermOperator;
import org.sopt.confeti.domain.elastic_search.infra.SearchTermRepository;
import org.sopt.confeti.domain.view.performance.application.dto.response.PerformanceInfo;
import org.sopt.confeti.global.resolver.music_api.artist.vo.ConfetiArtist;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SearchTermService {

    private final SearchTermOperator searchTermOperator;
    private final SearchTermRepository searchTermRepository;

    public void write(String searchTerm) {
        searchTermRepository.save(SearchTermDocument.create(searchTerm));
    }

    public void write(ConfetiArtist artist) {
        searchTermRepository.save(SearchTermDocument.create(artist.getName()));
    }

    public void write(Optional<ConfetiArtist> artist) {
        artist.ifPresent(
            confetiArtist -> searchTermRepository.save(
                SearchTermDocument.create(confetiArtist.getName()))
        );
    }

    public void write(Set<PerformanceInfo> performances) {
        searchTermRepository.saveAll(
            performances.stream()
                .map(PerformanceInfo::title)
                .map(SearchTermDocument::create)
                .toList()
        );
    }

    public List<PopularTermResult> getPopularSearchTerms(int limit) {
        return searchTermOperator.getPopularSearchTerms(limit);
    }
}
