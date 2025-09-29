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
                        responseCode = "401",
                        description = "Unauthorized",
                        content = @Content(
                                schema = @Schema(implementation = BaseResponse.class),
                                examples =
                                @ExampleObject(
                                        value =
                                                "{\n"
                                                        + "  \"status\": 401,\n"
                                                        + "  \"message\": \"사용자의 로그인 검증을 실패했습니다. | 잘못된 토큰입니다. | 만료된 토큰입니다. | 토큰이 없습니다. | 잘못된 토큰 형식입니다.\"\n"
                                                        + "}"
                                )
                        )
                ),
                @ApiResponse(
                        responseCode = "403",
                        description = "Forbidden",
                        content = @Content(
                                schema = @Schema(implementation = BaseResponse.class),
                                examples =
                                @ExampleObject(
                                        value =
                                                "{\n"
                                                        + "  \"status\": 403,\n"
                                                        + "  \"message\": \"리소스 접근 권한이 없습니다.\"\n"
                                                        + "}"
                                )
                        )
                )
        }
)
public @interface AuthErrorResponses {
}
