package org.sopt.confeti.global.common.constant;

import java.util.Arrays;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.sopt.confeti.global.exception.ConfetiException;
import org.sopt.confeti.global.message.ErrorMessage;

@Getter
@RequiredArgsConstructor
public enum PerformanceStatus {
    UPCOMING("upcoming"), ALL("all");
    
    private final String status;

    public static PerformanceStatus convert(final String input) {
        return Arrays.stream(PerformanceStatus.values())
                .filter(performanceType -> performanceType.getStatus().equalsIgnoreCase(input))
                .findFirst()
                .orElseThrow(
                        () -> new ConfetiException(ErrorMessage.BAD_REQUEST)
                );
    }
}
