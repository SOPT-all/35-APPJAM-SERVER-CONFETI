package org.sopt.confeti.global.message;

import static org.sopt.confeti.global.exception.ErrorResponseConstant.BAD_REQUEST_MESSAGE;
import static org.sopt.confeti.global.exception.ErrorResponseConstant.CONFLICT_MESSAGE;
import static org.sopt.confeti.global.exception.ErrorResponseConstant.EMPTY_TOKEN_MESSAGE;
import static org.sopt.confeti.global.exception.ErrorResponseConstant.EXPIRED_TOKEN_MESSAGE;
import static org.sopt.confeti.global.exception.ErrorResponseConstant.FORBIDDEN_MESSAGE;
import static org.sopt.confeti.global.exception.ErrorResponseConstant.INTERNAL_SERVER_ERROR_MESSAGE;
import static org.sopt.confeti.global.exception.ErrorResponseConstant.METHOD_NOT_ALLOWED_MESSAGE;
import static org.sopt.confeti.global.exception.ErrorResponseConstant.NOT_FOUND_MESSAGE;
import static org.sopt.confeti.global.exception.ErrorResponseConstant.TIMETABLE_FESTIVAL_IS_FULL_MESSAGE;
import static org.sopt.confeti.global.exception.ErrorResponseConstant.UNAUTHORIZED_MESSAGE;
import static org.sopt.confeti.global.exception.ErrorResponseConstant.UNPROCESSABLE_ENTITY_MESSAGE;
import static org.sopt.confeti.global.exception.ErrorResponseConstant.WRONG_TOKEN_MESSAGE;
import static org.sopt.confeti.global.exception.ErrorResponseConstant.WRONG_TOKEN_REQUEST_MESSAGE;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorMessage {
    /* 400 Bad Request */
    BAD_REQUEST(HttpStatus.BAD_REQUEST, BAD_REQUEST_MESSAGE),

    /* 401 Unauthorized */
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, UNAUTHORIZED_MESSAGE),
    WRONG_TOKEN(HttpStatus.UNAUTHORIZED, WRONG_TOKEN_MESSAGE),
    EXPIRED_TOKEN(HttpStatus.UNAUTHORIZED, EXPIRED_TOKEN_MESSAGE),
    EMPTY_TOKEN(HttpStatus.UNAUTHORIZED, EMPTY_TOKEN_MESSAGE),
    WRONG_TOKEN_REQUEST(HttpStatus.UNAUTHORIZED, WRONG_TOKEN_REQUEST_MESSAGE),


    /* 403 Forbidden*/
    FORBIDDEN(HttpStatus.FORBIDDEN, FORBIDDEN_MESSAGE),

    /* 404 Not Found */
    NOT_FOUND(HttpStatus.NOT_FOUND, NOT_FOUND_MESSAGE),
    EVENT_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 이벤트 타입입니다."),

    /* 405 Method Not Allowed */
    METHOD_NOT_ALLOWED(HttpStatus.METHOD_NOT_ALLOWED, METHOD_NOT_ALLOWED_MESSAGE),

    /* 409 Conflict */
    CONFLICT(HttpStatus.CONFLICT, CONFLICT_MESSAGE),
    TIMETABLE_FESTIVAL_IS_FULL(HttpStatus.CONFLICT, TIMETABLE_FESTIVAL_IS_FULL_MESSAGE),

    /* 422 Unprocessable Entity */
    TYPE_MISMATCH(HttpStatus.UNPROCESSABLE_ENTITY, UNPROCESSABLE_ENTITY_MESSAGE),

    /* 500 Internal Server Error*/
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, INTERNAL_SERVER_ERROR_MESSAGE),
    FAIL_JSON_SERIALIZATION(HttpStatus.INTERNAL_SERVER_ERROR, "JSON 직렬화에 실패했습니다."),
    FAIL_JSON_DESERIALIZATION(HttpStatus.INTERNAL_SERVER_ERROR, "JSON 역직렬화에 실패했습니다.");

    private final HttpStatus httpStatus;
    private final String message;

}
