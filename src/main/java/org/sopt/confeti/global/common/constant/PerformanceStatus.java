package org.sopt.confeti.global.common.constant;

import java.util.Arrays;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum PerformanceStatus {
    UPCOMING("upcoming"),
    ALL("all"),
    UNKNOWN("unknown")
    ;
    
    private final String status;

    public static PerformanceStatus from(final String input) {
        return Arrays.stream(PerformanceStatus.values())
                .filter(performanceType -> performanceType.getStatus().equalsIgnoreCase(input))
                .findFirst()
                .orElse(UNKNOWN);
    }
}
