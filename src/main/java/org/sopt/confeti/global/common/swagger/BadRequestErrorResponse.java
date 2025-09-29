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
        responseCode = "400",
        description = "Bad Request",
        content = @Content(
                schema = @Schema(implementation = BaseResponse.class),
                examples =
                @ExampleObject(
                        value =
                                "{\n"
                                        + "  \"status\": 400,\n"
                                        + "  \"message\": \"요청 형식이 올바르지 않습니다.\"\n"
                                        + "}"
                )
        )
)
public @interface BadRequestErrorResponse {
}
