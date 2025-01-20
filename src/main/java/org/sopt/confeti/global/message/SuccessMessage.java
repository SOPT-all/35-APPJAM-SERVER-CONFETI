package org.sopt.confeti.global.message;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum SuccessMessage {
    SUCCESS(HttpStatus.OK, "요청이 성공했습니다."),;

    private final HttpStatus httpStatus;
    private final String message;
}
