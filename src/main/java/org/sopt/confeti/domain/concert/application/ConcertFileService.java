package org.sopt.confeti.domain.concert.application;

import lombok.RequiredArgsConstructor;
import org.sopt.confeti.domain.concert.Concert;
import org.sopt.confeti.domain.concert.application.dto.ConcertFileInfo;
import org.sopt.confeti.global.common.CdnFileDomainResolveService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ConcertFileService {

    private final CdnFileDomainResolveService cdnFileDomainResolveService;

    public ConcertFileInfo getFileUrls(Concert concert) {
        return ConcertFileInfo.builder()
            .posterUrl(cdnFileDomainResolveService.resolve(concert.getPosterPath()))
            .build();
    }
}
