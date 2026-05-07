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

    /* 400 Bad Request - Festival Validation */
    public static final String FESTIVAL_INVALID_DURATION_MESSAGE = "시작 날짜는 종료 날짜보다 클 수 없습니다.";
    public static final String FESTIVAL_INVALID_RESERVE_DATE_MESSAGE = "예매 일자는 공연 시작일보다 이전이어야 합니다.";
    public static final String FESTIVAL_DATE_OUT_OF_RANGE_MESSAGE = "날짜가 공연 기간 범위를 벗어났습니다.";
    public static final String FESTIVAL_TIMETABLE_DATE_NO_STAGE_MESSAGE = "타임테이블이 존재하면 모든 날짜에 스테이지 정보가 있어야 합니다.";
    public static final String FESTIVAL_TIMETABLE_ARTIST_NOT_MAPPED_MESSAGE = "모든 아티스트가 타임테이블에 배정되어야 합니다.";
    public static final String FESTIVAL_TIMETABLE_OPEN_AT_AFTER_FIRST_TIME_MESSAGE = "티켓 오픈 시간은 첫 번째 공연 시작 시간 이전이어야 합니다.";
    public static final String FESTIVAL_TIMETABLE_STAGE_ORDER_DUPLICATE_MESSAGE = "스테이지 순서(order)가 중복됩니다.";
    public static final String FESTIVAL_TIMETABLE_TIME_START_AFTER_END_MESSAGE = "공연 시작 시간은 종료 시간보다 이전이어야 합니다.";
    public static final String FESTIVAL_TIMETABLE_TIME_OVERLAP_MESSAGE = "같은 스테이지 내 공연 시간이 겹칩니다.";
    public static final String FESTIVAL_TIMETABLE_NOT_SUPPORTED_MESSAGE = "타임테이블을 지원하지 않는 페스티벌입니다.";

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
