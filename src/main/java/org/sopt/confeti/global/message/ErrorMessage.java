package org.sopt.confeti.global.message;

import static org.sopt.confeti.global.exception.ErrorResponseConstant.BAD_REQUEST_MESSAGE;
import static org.sopt.confeti.global.exception.ErrorResponseConstant.CONFLICT_MESSAGE;
import static org.sopt.confeti.global.exception.ErrorResponseConstant.FESTIVAL_DATE_OUT_OF_RANGE_MESSAGE;
import static org.sopt.confeti.global.exception.ErrorResponseConstant.FESTIVAL_INVALID_DURATION_MESSAGE;
import static org.sopt.confeti.global.exception.ErrorResponseConstant.FESTIVAL_INVALID_RESERVE_DATE_MESSAGE;
import static org.sopt.confeti.global.exception.ErrorResponseConstant.FESTIVAL_TIMETABLE_ARTIST_NOT_MAPPED_MESSAGE;
import static org.sopt.confeti.global.exception.ErrorResponseConstant.FESTIVAL_TIMETABLE_DATE_NO_STAGE_MESSAGE;
import static org.sopt.confeti.global.exception.ErrorResponseConstant.FESTIVAL_TIMETABLE_OPEN_AT_AFTER_FIRST_TIME_MESSAGE;
import static org.sopt.confeti.global.exception.ErrorResponseConstant.FESTIVAL_TIMETABLE_STAGE_ORDER_DUPLICATE_MESSAGE;
import static org.sopt.confeti.global.exception.ErrorResponseConstant.FESTIVAL_TIMETABLE_TIME_OVERLAP_MESSAGE;
import static org.sopt.confeti.global.exception.ErrorResponseConstant.FESTIVAL_TIMETABLE_TIME_START_AFTER_END_MESSAGE;
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
    FESTIVAL_INVALID_DURATION(HttpStatus.BAD_REQUEST, FESTIVAL_INVALID_DURATION_MESSAGE),
    FESTIVAL_INVALID_RESERVE_DATE(HttpStatus.BAD_REQUEST, FESTIVAL_INVALID_RESERVE_DATE_MESSAGE),
    FESTIVAL_DATE_OUT_OF_RANGE(HttpStatus.BAD_REQUEST, FESTIVAL_DATE_OUT_OF_RANGE_MESSAGE),
    FESTIVAL_TIMETABLE_DATE_NO_STAGE(HttpStatus.BAD_REQUEST, FESTIVAL_TIMETABLE_DATE_NO_STAGE_MESSAGE),
    FESTIVAL_TIMETABLE_ARTIST_NOT_MAPPED(HttpStatus.BAD_REQUEST, FESTIVAL_TIMETABLE_ARTIST_NOT_MAPPED_MESSAGE),
    FESTIVAL_TIMETABLE_OPEN_AT_AFTER_FIRST_TIME(HttpStatus.BAD_REQUEST, FESTIVAL_TIMETABLE_OPEN_AT_AFTER_FIRST_TIME_MESSAGE),
    FESTIVAL_TIMETABLE_STAGE_ORDER_DUPLICATE(HttpStatus.BAD_REQUEST, FESTIVAL_TIMETABLE_STAGE_ORDER_DUPLICATE_MESSAGE),
    FESTIVAL_TIMETABLE_TIME_START_AFTER_END(HttpStatus.BAD_REQUEST, FESTIVAL_TIMETABLE_TIME_START_AFTER_END_MESSAGE),
    FESTIVAL_TIMETABLE_TIME_OVERLAP(HttpStatus.BAD_REQUEST, FESTIVAL_TIMETABLE_TIME_OVERLAP_MESSAGE),

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

    /* 405 Method Not Allowed */
    METHOD_NOT_ALLOWED(HttpStatus.METHOD_NOT_ALLOWED, METHOD_NOT_ALLOWED_MESSAGE),

    /* 409 Conflict */
    CONFLICT(HttpStatus.CONFLICT, CONFLICT_MESSAGE),
    TIMETABLE_FESTIVAL_IS_FULL(HttpStatus.CONFLICT, TIMETABLE_FESTIVAL_IS_FULL_MESSAGE),

    /* 422 Unprocessable Entity */
    TYPE_MISMATCH(HttpStatus.UNPROCESSABLE_ENTITY, UNPROCESSABLE_ENTITY_MESSAGE),

    /* 500 Internal Server Error*/
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, INTERNAL_SERVER_ERROR_MESSAGE);

    private final HttpStatus httpStatus;
    private final String message;

}
