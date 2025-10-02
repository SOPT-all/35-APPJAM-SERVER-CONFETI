package org.sopt.confeti.global.common.swagger;

import static org.sopt.confeti.global.exception.ErrorResponseConstant.*;

import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.sopt.confeti.global.common.BaseResponse;
import org.sopt.confeti.global.exception.ErrorResponseConstant;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@ApiResponse(
    responseCode = UNPROCESSABLE_ENTITY_STATUS,
    description = UNAUTHORIZED_DESCRIPTION,
    content = @Content(
        schema = @Schema(implementation = BaseResponse.class),
        examples =
        @ExampleObject(
            value =
                "{\n"
                    + "  \"status\": " + UNPROCESSABLE_ENTITY_STATUS + ",\n"
                    + "  \"message\": \"" + UNAUTHORIZED_MESSAGE + "\"\n"
                    + "}"
        )
    )
)
public @interface TypeMismatchErrorResponse {
}
