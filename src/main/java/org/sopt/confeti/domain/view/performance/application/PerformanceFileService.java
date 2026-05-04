package org.sopt.confeti.domain.view.performance.application;

import lombok.RequiredArgsConstructor;
import org.sopt.confeti.domain.view.performance.Performance;
import org.sopt.confeti.domain.view.performance.PerformanceFileInfo;
import org.sopt.confeti.global.common.CdnFileDomainResolveService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PerformanceFileService {

    private final CdnFileDomainResolveService cdnFileDomainResolveService;

    public PerformanceFileInfo getFileInfo(final Performance performance) {
        return PerformanceFileInfo.builder()
            .posterUrl(cdnFileDomainResolveService.resolve(performance.getPosterPath()))
            .build();
    }

    public PerformanceFileInfo getFileInfo(final String posterPath) {
        return PerformanceFileInfo.builder()
            .posterUrl(cdnFileDomainResolveService.resolve(posterPath))
            .build();
    }
}
