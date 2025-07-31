package org.sopt.confeti.global.mapper;

import lombok.RequiredArgsConstructor;
import org.sopt.confeti.domain.performance.Performance;
import org.sopt.confeti.global.mapper.dto.concert.Concert;
import org.sopt.confeti.global.mapper.dto.festival.Festival;
import org.sopt.confeti.global.util.S3FileHandler;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PerformanceMapper {

    private final S3FileHandler s3FileHandler;

    public Festival toFestival(Performance performance) {
        return Festival.toFestival(performance, s3FileHandler);
    }

    public Concert toConcert(Performance performance) {
        return Concert.toConcert(performance, s3FileHandler);
    }
}
