package org.sopt.confeti.global.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import org.sopt.confeti.global.message.ErrorMessage;
import org.sopt.confeti.global.message.SuccessMessage;

@Getter
public class BaseResponse<T> {

    private final int status;
    private final String message;
    @JsonInclude(value = JsonInclude.Include.NON_NULL)
    private final T data;

    @Schema(hidden = true)
    @JsonInclude(value = JsonInclude.Include.NON_DEFAULT)
    private final Exception exception;

    private BaseResponse(Builder<T> builder) {
        this.status = builder.status;
        this.message = builder.message;
        this.data = builder.data;
        this.exception = builder.exception;
    }

    public static BaseResponse<Void> of(SuccessMessage successMessage) {
        return BaseResponse.<Void>builder()
            .status(successMessage.getHttpStatus().value())
            .message(successMessage.getMessage())
            .build();
    }

    public static <T> BaseResponse<T> of(SuccessMessage successMessage, T data) {
        return BaseResponse.<T>builder()
            .status(successMessage.getHttpStatus().value())
            .message(successMessage.getMessage())
            .data(data)
            .build();
    }

    public static BaseResponse<Void> of(ErrorMessage errorMessage) {
        return BaseResponse.<Void>builder()
            .status(errorMessage.getHttpStatus().value())
            .message(errorMessage.getMessage())
            .build();
    }

    public static <T> Builder<T> builder() {
        return new Builder<>();
    }

    public static class Builder<T> {

        private int status;
        private String message;
        private T data;
        private Exception exception;

        public Builder<T> status(int status) {
            this.status = status;
            return this;
        }

        public Builder<T> message(String message) {
            this.message = message;
            return this;
        }

        public Builder<T> data(T data) {
            this.data = data;
            return this;
        }

        public Builder<T> exception(Exception exception) {
            this.exception = exception;
            return this;
        }

        public BaseResponse<T> build() {
            return new BaseResponse<T>(this);
        }
    }
}
