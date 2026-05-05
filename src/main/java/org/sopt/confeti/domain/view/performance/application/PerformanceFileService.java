package org.sopt.confeti.domain.view.performance.application;

import lombok.RequiredArgsConstructor;
import org.sopt.confeti.domain.elastic_search.application.dto.response.SearchPerformanceResult;
import org.sopt.confeti.domain.view.performance.Performance;
import org.sopt.confeti.domain.view.performance.PerformanceFileInfo;
import org.sopt.confeti.domain.view.performance.application.dto.response.PerformancePreviewDTO;
import org.sopt.confeti.global.common.CdnFileDomainResolveService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PerformanceFileService {

    private final CdnFileDomainResolveService cdnFileDomainResolveService;

    public PerformanceFileInfo getFileInfo(Performance performance) {
        return PerformanceFileInfo.builder()
            .posterUrl(cdnFileDomainResolveService.resolve(performance.getPosterPath()))
            .build();
    }

    public PerformanceFileInfo getFileInfo(SearchPerformanceResult performance) {
        return PerformanceFileInfo.builder()
            .posterUrl(performance.posterPath())
            .build();
    }

    public PerformanceFileInfo getFileInfo(PerformancePreviewDTO performancePreview) {
        return PerformanceFileInfo.builder()
            .posterUrl(performancePreview.posterPath())
            .build();
    }
}
