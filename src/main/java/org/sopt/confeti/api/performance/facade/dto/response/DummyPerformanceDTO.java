package org.sopt.confeti.api.performance.facade.dto.response;

import java.time.LocalDateTime;
import org.sopt.confeti.domain.concert.Concert;
import org.sopt.confeti.domain.festival.Festival;
import org.sopt.confeti.global.common.constant.PerformanceType;

// 더미 데이터 고정값! - 스프린트 이후로 지우기
public record DummyPerformanceDTO(
        long typeId,
        PerformanceType type,
        String subtitle,
        LocalDateTime reserveAt,
        String reservationBgUrl
) {
    public static DummyPerformanceDTO from(final Concert concert) {
        return new DummyPerformanceDTO(
                concert.getId(),
                PerformanceType.CONCERT,
                concert.getConcertSubtitle(),
                concert.getReserveAt(),
                concert.getConcertReservationBgPath()
        );
    }

    public static DummyPerformanceDTO from(final Festival festival) {
        return new DummyPerformanceDTO(
                festival.getId(),
                PerformanceType.FESTIVAL,
                festival.getFestivalSubtitle(),
                festival.getReserveAt(),
                festival.getFestivalReservationBgPath()
        );
    }
}
// 더미 데이터 고정값! - 스프린트 이후로 지우기
