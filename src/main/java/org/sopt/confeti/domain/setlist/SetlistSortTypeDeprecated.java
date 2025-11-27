package org.sopt.confeti.domain.setlist;

import java.util.Arrays;

@Deprecated
public enum SetlistSortTypeDeprecated {
    OLDEST("oldestFirst"),
    LATEST("createdAt");

    private final String value;

    SetlistSortTypeDeprecated(String value) {
        this.value = value;
    }

    public static SetlistSortTypeDeprecated from(String value) {
        if (value == null || value.isBlank()) {
            return OLDEST;
        }
        return Arrays.stream(values())
            .filter(type -> type.value.equalsIgnoreCase(value))
            .findFirst()
            .orElse(OLDEST);
    }

    public String getValue() {
        return value;
    }
}

