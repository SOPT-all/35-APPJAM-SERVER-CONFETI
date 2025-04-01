package org.sopt.confeti.global.util;

import org.sopt.confeti.global.common.BaseResponse;
import org.sopt.confeti.global.message.ErrorMessage;
import org.sopt.confeti.global.message.SuccessMessage;
import org.springframework.http.ResponseEntity;
import reactor.core.publisher.Mono;

public interface ApiResponseUtil {
    static ResponseEntity<BaseResponse<?>> success(SuccessMessage successMessage) {
        return ResponseEntity.status(successMessage.getHttpStatus())
                .body(BaseResponse.of(successMessage));
    }

    static <T> ResponseEntity<BaseResponse<?>> success(SuccessMessage successMessage, T data) {
        return ResponseEntity.status(successMessage.getHttpStatus())
                .body(BaseResponse.of(successMessage, data));
    }

    static <T> Mono<ResponseEntity<BaseResponse<?>>> success(SuccessMessage successMessage, Mono<T> data) {
        return data.map(result ->
                ResponseEntity.status(successMessage.getHttpStatus())
                        .body(BaseResponse.of(successMessage, result))
        );
    }

    static ResponseEntity<BaseResponse<?>> failure(ErrorMessage errorMessage) {
        return ResponseEntity.status(errorMessage.getHttpStatus())
                .body(BaseResponse.of(errorMessage));
    }
}
