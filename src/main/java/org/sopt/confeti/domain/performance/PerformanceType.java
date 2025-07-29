package org.sopt.confeti.domain.performance;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum PerformanceType {
    CONCERT("concert"), FESTIVAL("festival");

    private final String name;
}
