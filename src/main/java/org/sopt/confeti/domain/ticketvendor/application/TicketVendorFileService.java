package org.sopt.confeti.domain.ticketvendor.application;

import lombok.RequiredArgsConstructor;
import org.sopt.confeti.domain.ticketvendor.TicketVendorFileInfo;
import org.sopt.confeti.domain.ticketvendor.application.dto.response.TicketVendorDto;
import org.sopt.confeti.global.common.CdnFileDomainResolveService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TicketVendorFileService {

    private final CdnFileDomainResolveService cdnFileDomainResolveService;

    public TicketVendorFileInfo getFileInfo(TicketVendorDto dto) {
        return TicketVendorFileInfo.builder()
            .logoUrl(cdnFileDomainResolveService.resolve(dto.logoPath()))
            .build();
    }
}
