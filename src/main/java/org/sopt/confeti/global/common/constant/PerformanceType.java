package org.sopt.confeti.global.common.constant;

import java.util.Arrays;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.sopt.confeti.global.exception.ConfetiException;
import org.sopt.confeti.global.message.ErrorMessage;

@Getter
@AllArgsConstructor
public enum PerformanceType {
    CONCERT("concert"), FESTIVAL("festival");

    private final String type;

    public static PerformanceType convert(final String input) {
        return Arrays.stream(PerformanceType.values())
                .filter(performanceType -> performanceType.getType().equals(input))
                .findFirst()
                .orElseThrow(
                        () -> new ConfetiException(ErrorMessage.BAD_REQUEST)
                );
    }
}
