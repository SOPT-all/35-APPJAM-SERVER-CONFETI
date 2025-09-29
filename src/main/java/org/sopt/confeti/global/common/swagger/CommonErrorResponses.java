package org.sopt.confeti.global.common.swagger;

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

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@ApiResponses(
        value = {
                @ApiResponse(
                        responseCode = "404",
                        description = "Not Found",
                        content = @Content(
                                schema = @Schema(implementation = BaseResponse.class),
                                examples =
                                        @ExampleObject(
                                                value =
                                                        "{\n"
                                                                  + "  \"status\": 404,\n"
                                                                  + "  \"message\": \"요청하는 리소스가 존재하지 않습니다.\"\n"
                                                        + "}"
                                        )
                        )
                ),
                @ApiResponse(
                        responseCode = "500",
                        description = "Internal Server Error",
                        content = @Content(
                                schema = @Schema(implementation = BaseResponse.class),
                                examples =
                                @ExampleObject(
                                        value =
                                                "{\n"
                                                        + "  \"status\": 500,\n"
                                                        + "  \"message\": \"서버 내부 오류입니다.\"\n"
                                                        + "}"
                                )
                        )
                )
        }
)
public @interface CommonErrorResponses {
}
