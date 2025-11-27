package org.sopt.confeti.domain.setlist;

import java.util.Arrays;
import java.util.Optional;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum SetlistSortType {
    EARLIEST("earliest"),
    LATEST("latest");

    private final String name;

    public static SetlistSortType getDefault() {
        return EARLIEST;
    }

    public static Optional<SetlistSortType> from(String value) {
        return Arrays.stream(values())
            .filter(sortType -> sortType.name.equalsIgnoreCase(value))
            .findFirst();
    }
}
