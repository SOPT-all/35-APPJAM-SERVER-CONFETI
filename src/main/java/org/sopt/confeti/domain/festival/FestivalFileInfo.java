package org.sopt.confeti.domain.festival;

import java.util.Map;
import lombok.Builder;
import org.sopt.confeti.domain.festival_reservation_url.FestivalReservationFileInfo;

@Builder
public record FestivalFileInfo(
    String posterUrl,
    String logoUrl,
    Map<Long, FestivalReservationFileInfo> festivalReservationFileInfoMap
) {

}
