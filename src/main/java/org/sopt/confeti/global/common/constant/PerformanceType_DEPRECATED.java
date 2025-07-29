package org.sopt.confeti.global.common.constant;

import java.util.Arrays;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.sopt.confeti.global.exception.ConfetiException;
import org.sopt.confeti.global.message.ErrorMessage;

@Getter
@AllArgsConstructor
public enum PerformanceType_DEPRECATED {
    CONCERT("concert"), FESTIVAL("festival"), PERFORMANCE("performance");

    private final String type;

    public static PerformanceType_DEPRECATED convert(final String input) {
        return Arrays.stream(PerformanceType_DEPRECATED.values())
                .filter(performanceType -> performanceType.getType().equalsIgnoreCase(input))
                .findFirst()
                .orElseThrow(
                        () -> new ConfetiException(ErrorMessage.BAD_REQUEST)
                );
    }
}
