package org.sopt.confeti.domain.festival.application;

import java.util.LinkedHashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.sopt.confeti.domain.festival.Festival;
import org.sopt.confeti.domain.festival.FestivalFileInfo;
import org.sopt.confeti.domain.festival_reservation_url.FestivalReservationFileInfo;
import org.sopt.confeti.domain.festival_reservation_url.application.FestivalReservationFileService;
import org.sopt.confeti.global.common.CdnFileDomainResolveService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FestivalFileService {

    private final CdnFileDomainResolveService cdnFileDomainResolveService;
    private final FestivalReservationFileService festivalReservationFileService;

    public FestivalFileInfo getFileInfo(Festival festival) {
        Map<Long, FestivalReservationFileInfo> reservationFileInfoMap =
            new LinkedHashMap<>(festival.getReservationUrls().size());
        festival.getReservationUrls().forEach(reservationUrl ->
            reservationFileInfoMap.put(
                reservationUrl.getId(),
                festivalReservationFileService.getFileInfo(reservationUrl)
            )
        );

        return FestivalFileInfo.builder()
            .posterUrl(cdnFileDomainResolveService.resolve(festival.getPosterPath()))
            .logoUrl(cdnFileDomainResolveService.resolve(festival.getLogoPath()))
            .festivalReservationFileInfoMap(reservationFileInfoMap)
            .build();
    }

    public FestivalFileInfo getBasicFileInfo(Festival festival) {
        return FestivalFileInfo.builder()
            .posterUrl(cdnFileDomainResolveService.resolve(festival.getPosterPath()))
            .logoUrl(cdnFileDomainResolveService.resolve(festival.getLogoPath()))
            .festivalReservationFileInfoMap(Map.of())
            .build();
    }
}
