package org.sopt.confeti.global.message;

import org.springframework.http.HttpStatus;

public enum SuccessMessage {
    SUCCESS(HttpStatus.OK, "요청이 성공했습니다."),;

    private final HttpStatus httpStatus;
    private final String message;

    private SuccessMessage(HttpStatus httpStatus, String message) {
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
