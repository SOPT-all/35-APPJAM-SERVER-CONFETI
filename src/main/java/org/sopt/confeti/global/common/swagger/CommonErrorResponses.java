package org.sopt.confeti.global.common.swagger;

import static org.sopt.confeti.global.exception.ErrorResponseConstant.*;

import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.sopt.confeti.global.common.BaseResponse;
import org.sopt.confeti.global.exception.ErrorResponseConstant;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@ApiResponses(
    value = {
        @ApiResponse(
            responseCode = NOT_FOUND_STATUS,
            description = NOT_FOUND_DESCRIPTION,
            content = @Content(
                schema = @Schema(implementation = BaseResponse.class),
                examples =
                @ExampleObject(
                    value =
                        "{\n"
                            + "  \"status\": " + NOT_FOUND_STATUS + ",\n"
                            + "  \"message\": \"" + NOT_FOUND_MESSAGE + "\"\n"
                            + "}"
                )
            )
        ),
        @ApiResponse(
            responseCode = INTERNAL_SERVER_ERROR_STATUS,
            description = INTERNAL_SERVER_ERROR_DESCRIPTION,
            content = @Content(
                schema = @Schema(implementation = BaseResponse.class),
                examples =
                @ExampleObject(
                    value =
                        "{\n"
                            + "  \"status\": " + INTERNAL_SERVER_ERROR_STATUS + ",\n"
                            + "  \"message\": \"" + INTERNAL_SERVER_ERROR_MESSAGE + "\"\n"
                            + "}"
                )
            )
        )
    }
)
public @interface CommonErrorResponses {
}
