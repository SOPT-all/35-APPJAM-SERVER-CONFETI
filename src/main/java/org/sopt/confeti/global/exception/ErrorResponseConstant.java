package org.sopt.confeti.global.exception;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ErrorResponseConstant {

    /* 400 Bad Request */
//    public static final String BAD_REQUEST_STATUS = String.valueOf(HttpStatus.BAD_REQUEST.value());
    public static final String BAD_REQUEST_STATUS = "400";
    public static final String BAD_REQUEST_DESCRIPTION = "Bad Request";
    public static final String BAD_REQUEST_MESSAGE = "요청 형식이 올바르지 않습니다.";

    /* 401 Unauthorized */
    public static final String UNAUTHORIZED_STATUS = "401";
    public static final String UNAUTHORIZED_DESCRIPTION = "Unauthorized";
    public static final String UNAUTHORIZED_MESSAGE = "사용자의 로그인 검증을 실패했습니다.";
    public static final String WRONG_TOKEN_MESSAGE = "잘못된 토큰입니다.";
    public static final String WRONG_TOKEN_REQUEST_MESSAGE = "잘못된 토큰 형식입니다.";
    public static final String EXPIRED_TOKEN_MESSAGE = "만료된 토큰입니다.";
    public static final String EMPTY_TOKEN_MESSAGE = "토큰이 없습니다.";

    /* 403 Forbidden*/
    public static final String FORBIDDEN_STATUS = "403";
    public static final String FORBIDDEN_DESCRIPTION = "Forbidden";
    public static final String FORBIDDEN_MESSAGE = "리소스 접근 권한이 없습니다.";

    /* 404 Not Found */
    public static final String NOT_FOUND_STATUS = "404";
    public static final String NOT_FOUND_DESCRIPTION = "Not Found";
    public static final String NOT_FOUND_MESSAGE = "요청하는 리소스가 존재하지 않습니다.";

    /* 405 Method Not Allowed */
    public static final String METHOD_NOT_ALLOWED_STATUS = "405";
    public static final String METHOD_NOT_ALLOWED_DESCRIPTION = "Method Not Allowed";
    public static final String METHOD_NOT_ALLOWED_MESSAGE = "잘못된 HTTP Method 요청입니다.";

    /* 409 Conflict */
    public static final String CONFLICT_STATUS = "409";
    public static final String CONFLICT_DESCRIPTION = "Conflict";
    public static final String CONFLICT_MESSAGE = "이미 존재하는 리소스입니다.";
    public static final String TIMETABLE_FESTIVAL_IS_FULL_MESSAGE = "더 이상 등록할 수 없습니다.";

    /* 422 Unprocessable Entity */
    public static final String UNPROCESSABLE_ENTITY_STATUS = "422";
    public static final String UNPROCESSABLE_ENTITY_DESCRIPTION = "Unprocessable Entity";
    public static final String UNPROCESSABLE_ENTITY_MESSAGE = "올바르지 않은 쿼리 파라미터 형식입니다.";

    /* 500 Internal Server Error*/
    public static final String INTERNAL_SERVER_ERROR_STATUS = "500";
    public static final String INTERNAL_SERVER_ERROR_DESCRIPTION = "Internal Server Error";
    public static final String INTERNAL_SERVER_ERROR_MESSAGE = "서버 내부 오류입니다.";
}
