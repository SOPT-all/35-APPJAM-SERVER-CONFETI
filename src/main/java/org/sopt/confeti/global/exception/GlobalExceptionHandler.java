package org.sopt.confeti.global.exception;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import org.sopt.confeti.global.common.BaseResponse;
import org.sopt.confeti.global.message.ErrorMessage;
import org.sopt.confeti.global.util.ApiResponseUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<BaseResponse<Void>> handleIllegalArgumentException(
        IllegalArgumentException e, HttpServletRequest request) {
        request.setAttribute("exception", e);
        return ApiResponseUtil.failure(ErrorMessage.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<BaseResponse<Void>> handleMethodArgumentTypeMismatchException(
        MethodArgumentTypeMismatchException e, HttpServletRequest request) {
        request.setAttribute("exception", e);
        return ApiResponseUtil.failure(ErrorMessage.TYPE_MISMATCH);
    }

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<BaseResponse<Void>> handleUnauthorizedException(UnauthorizedException e,
        HttpServletRequest request) {
        request.setAttribute("exception", e);
        return ApiResponseUtil.failure(ErrorMessage.UNAUTHORIZED);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<BaseResponse<Void>> handleNotFoundException(NotFoundException e,
        HttpServletRequest request) {
        request.setAttribute("exception", e);
        return ApiResponseUtil.failure(ErrorMessage.NOT_FOUND);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<BaseResponse<Void>> handleHttpRequestMethodNotSupportedException(
        HttpRequestMethodNotSupportedException e, HttpServletRequest request) {
        request.setAttribute("exception", e);
        return ApiResponseUtil.failure(ErrorMessage.METHOD_NOT_ALLOWED);
    }

    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<BaseResponse<Void>> handleConflictException(ConflictException e,
        HttpServletRequest request) {
        request.setAttribute("exception", e);
        return ApiResponseUtil.failure(ErrorMessage.CONFLICT);
    }

    @ExceptionHandler(ConfetiException.class)
    public ResponseEntity<BaseResponse<Void>> handleConfetiException(ConfetiException e,
        HttpServletRequest request) {
        request.setAttribute("exception", e);
        return ApiResponseUtil.failure(e.getErrorMessage());
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<BaseResponse<Void>> handleConstraintViolationException(
        ConstraintViolationException e, HttpServletRequest request) {
        request.setAttribute("exception", e);
        return ApiResponseUtil.failure(ErrorMessage.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<BaseResponse<Void>> handleException(Exception e,
        HttpServletRequest request) {
        request.setAttribute("exception", e);
        return ApiResponseUtil.failure(ErrorMessage.INTERNAL_SERVER_ERROR);
    }
}
