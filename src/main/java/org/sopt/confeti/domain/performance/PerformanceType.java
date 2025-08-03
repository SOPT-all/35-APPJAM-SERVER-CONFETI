package org.sopt.confeti.domain.performance;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.sopt.confeti.global.exception.ConfetiException;
import org.sopt.confeti.global.message.ErrorMessage;

import java.util.Arrays;

@Getter
@RequiredArgsConstructor
public enum PerformanceType {
    PERFORMANCE("performance"), CONCERT("concert"), FESTIVAL("festival");

    private final String name;

    public static PerformanceType convert(final String input) {
        return Arrays.stream(PerformanceType.values())
                .filter(performanceType -> performanceType.getName().equalsIgnoreCase(input))
                .findFirst()
                .orElseThrow(
                        () -> new ConfetiException(ErrorMessage.BAD_REQUEST)
                );
    }
}
