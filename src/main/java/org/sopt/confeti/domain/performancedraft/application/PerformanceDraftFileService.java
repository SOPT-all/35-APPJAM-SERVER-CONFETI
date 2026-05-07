package org.sopt.confeti.domain.performancedraft.application;

import lombok.RequiredArgsConstructor;
import org.sopt.confeti.domain.performancedraft.PerformanceDraftFileInfo;
import org.sopt.confeti.domain.performancedraft.application.dto.response.PerformanceDraftDto;
import org.sopt.confeti.global.common.CdnFileDomainResolveService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PerformanceDraftFileService {

    private final CdnFileDomainResolveService cdnFileDomainResolveService;

    public PerformanceDraftFileInfo getFileInfo(PerformanceDraftDto draft) {
        return PerformanceDraftFileInfo.builder()
            .posterUrl(cdnFileDomainResolveService.resolve(draft.posterPath()))
            .logoUrl(cdnFileDomainResolveService.resolve(draft.logoPath()))
            .build();
    }
}
