package org.sopt.confeti.global.message;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum TimetableFestivalErrorMessage {
    /* 409 Conflict */
    CONFLICT(HttpStatus.CONFLICT, "페스티벌 등록에 실패했습니다.");

    private final HttpStatus httpStatus;
    private final String message;

    TimetableFestivalErrorMessage(HttpStatus httpStatus, String message) {
        this.httpStatus = httpStatus;
        this.message = message;
    }
}
