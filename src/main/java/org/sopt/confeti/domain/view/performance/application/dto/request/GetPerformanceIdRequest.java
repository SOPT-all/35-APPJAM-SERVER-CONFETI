package org.sopt.confeti.domain.view.performance.application.dto.request;

import org.sopt.confeti.domain.concert.Concert;
import org.sopt.confeti.domain.festival.Festival;
import org.sopt.confeti.global.common.constant.PerformanceType;

public record GetPerformanceIdRequest(
        long typeId,
        PerformanceType type
) {
    public static GetPerformanceIdRequest from(final Festival festival) {
        return new GetPerformanceIdRequest(
                festival.getId(),
                PerformanceType.FESTIVAL
        );
    }

    public static GetPerformanceIdRequest from(final Concert concert) {
        return new GetPerformanceIdRequest(
                concert.getId(),
                PerformanceType.CONCERT
        );
    }
}
