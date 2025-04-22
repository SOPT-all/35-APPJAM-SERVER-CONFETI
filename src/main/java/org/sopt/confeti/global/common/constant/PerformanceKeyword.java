package org.sopt.confeti.global.common.constant;

import java.util.Arrays;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.sopt.confeti.global.exception.ConfetiException;
import org.sopt.confeti.global.message.ErrorMessage;

@RequiredArgsConstructor
public enum PerformanceKeyword {
    CONCERT(
            List.of("concert", "concerts", "콘서트"),
            PerformanceType.CONCERT
    ),
    FESTIVAL(
            List.of("festival", "festivals", "페스티벌", "패스티벌"),
            PerformanceType.FESTIVAL
    ),
    PERFORMANCE(
            List.of("performance", "performances", "공연"),
            PerformanceType.PERFORMANCE
    );

    private final List<String> keywords;
    private final PerformanceType performanceType;

    public static boolean isValid(String input) {
        return Arrays.stream(PerformanceKeyword.values())
                .flatMap(performanceKeyword -> performanceKeyword.keywords.stream())
                .anyMatch(keyword -> keyword.equalsIgnoreCase(input));
    }

    public static PerformanceType getMatchingPerformanceType(String input) {
        return Arrays.stream(PerformanceKeyword.values())
                .filter(performanceKeyword ->
                        performanceKeyword.keywords.stream()
                                .anyMatch(keyword -> keyword.equalsIgnoreCase(input))
                )
                .findFirst()
                .map(performanceKeyword -> performanceKeyword.performanceType)
                .orElseThrow(
                        () -> new ConfetiException(ErrorMessage.BAD_REQUEST)
                );
    }
}
