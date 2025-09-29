package org.sopt.confeti.global.common.swagger;

import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.sopt.confeti.global.common.BaseResponse;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@ApiResponse(
    responseCode = "422",
    description = "Type Mismatch",
    content = @Content(
        schema = @Schema(implementation = BaseResponse.class),
        examples =
        @ExampleObject(
            value =
                "{\n"
                    + "  \"status\": 422,\n"
                    + "  \"message\": \"올바르지 않은 쿼리 파라미터 형식입니다.\"\n"
                    + "}"
        )
    )
)
public @interface TypeMismatchErrorResponse {

}
