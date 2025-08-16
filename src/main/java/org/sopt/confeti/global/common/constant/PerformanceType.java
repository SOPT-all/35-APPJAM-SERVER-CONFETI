package org.sopt.confeti.global.common.constant;

import java.util.Arrays;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.sopt.confeti.global.exception.ConfetiException;
import org.sopt.confeti.global.message.ErrorMessage;

@Getter
@AllArgsConstructor
public enum PerformanceType {
    CONCERT("concert"), FESTIVAL("festival"), PERFORMANCE("performance");

    @JsonValue
    private final String name;

    @JsonCreator
    public static PerformanceType convert(final String input) {
        return Arrays.stream(PerformanceType.values())
                .filter(performanceType -> performanceType.getName().equalsIgnoreCase(input))
                .findFirst()
                .orElseThrow(
                        () -> new ConfetiException(ErrorMessage.BAD_REQUEST)
                );
    }
}
