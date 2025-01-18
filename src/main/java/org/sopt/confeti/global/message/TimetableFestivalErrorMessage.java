package org.sopt.confeti.global.message;

import org.springframework.http.HttpStatus;

public enum TimetableFestivalErrorMessage {
    /* 409 Conflict */
    CONFLICT(HttpStatus.CONFLICT, "페스티벌 등록에 실패했습니다.");

    private final HttpStatus httpStatus;
    private final String message;

    TimetableFestivalErrorMessage(HttpStatus httpStatus, String message) {
        this.httpStatus = httpStatus;
        this.message = message;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public String getMessage() {
        return message;
    }
}
