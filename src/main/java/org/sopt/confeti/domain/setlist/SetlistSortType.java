package org.sopt.confeti.domain.setlist;

import java.util.Arrays;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum SetlistSortType {
    EARLIEST("earliest"),
    LATEST("latest"),
    UNKNOWN("unknown");

    private final String value;

    public static SetlistSortType from(String value) {
        return Arrays.stream(values())
            .filter(sortType -> sortType.value.equalsIgnoreCase(value))
            .findFirst()
            .orElse(UNKNOWN);
    }
}
