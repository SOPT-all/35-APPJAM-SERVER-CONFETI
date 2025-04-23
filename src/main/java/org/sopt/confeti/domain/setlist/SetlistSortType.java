package org.sopt.confeti.domain.setlist;

import java.util.Arrays;

public enum SetlistSortType {
    OLDEST("oldest"),
    LATEST("latest");

    private final String value;

    SetlistSortType(String value) {
        this.value = value;
    }

    public static SetlistSortType from(String value) {
        if (value == null || value.isBlank())
            return OLDEST;
        return Arrays.stream(values())
                .filter(type -> type.value.equalsIgnoreCase(value))
                .findFirst()
                .orElse(OLDEST);
    }

    public String getValue() {
        return value;
    }
}

