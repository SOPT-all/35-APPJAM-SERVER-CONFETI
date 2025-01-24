package org.sopt.confeti.api.performance.facade.dto.response;

import java.util.stream.IntStream;
import org.sopt.confeti.domain.view.performance.PerformanceTicketDTO;

import java.util.List;

public record PerformanceReservationDTO(
        List<PerformanceReservationDetailDTO> performanceReservation
) {
    public static PerformanceReservationDTO from (List<PerformanceTicketDTO> performanceTickets){
        return new PerformanceReservationDTO(
                performanceTickets.stream()
                        .map(PerformanceReservationDetailDTO::from)
                        .toList()
        );
    }

    // 더미 데이터 고정값! - 스프린트 이후로 지우기
    public static PerformanceReservationDTO toDummy(final List<DummyPerformanceDTO> dummyPerformanceDTOS) {
        return new PerformanceReservationDTO(
                IntStream.range(0, dummyPerformanceDTOS.size())
                        .mapToObj(i -> PerformanceReservationDetailDTO.of(dummyPerformanceDTOS.get(i), i))
                        .toList()
        );
    }
    // 더미 데이터 고정값! - 스프린트 이후로 지우기
}
