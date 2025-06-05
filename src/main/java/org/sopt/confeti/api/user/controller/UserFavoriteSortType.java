package org.sopt.confeti.api.user.controller;

import org.sopt.confeti.global.exception.ConfetiException;
import org.sopt.confeti.global.message.ErrorMessage;

import java.util.Arrays;

public enum UserFavoriteSortType {
    CREATED_AT("createdAt"),
    ALPHABETICALLY("alphabetically");

    private final String value;

    UserFavoriteSortType(String value) {
        this.value = value;
    }

    public static UserFavoriteSortType from(String value) {
        return Arrays.stream(values())
                .filter(type -> type.value.equalsIgnoreCase(value))
                .findFirst()
                .orElseThrow(() -> new ConfetiException(ErrorMessage.BAD_REQUEST));
    }

    public String getValue() {
        return value;
    }
}
