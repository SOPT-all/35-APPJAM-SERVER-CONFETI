package org.sopt.confeti.api.user.controller;

import org.sopt.confeti.global.exception.ConfetiException;
import org.sopt.confeti.global.message.ErrorMessage;

import java.util.Arrays;

public enum UserTimetableSortType {
    OLDEST_FIRST("oldestFirst"),
    CREATED_AT("createdAt");

    private final String value;

    UserTimetableSortType(String value) {
        this.value = value;
    }

    public static UserTimetableSortType from(String value) {
        return Arrays.stream(values())
                .filter(type -> type.value.equalsIgnoreCase(value))
                .findFirst()
                .orElseThrow(() -> new ConfetiException(ErrorMessage.BAD_REQUEST));
    }

    public String getValue() {
        return value;
    }
}
