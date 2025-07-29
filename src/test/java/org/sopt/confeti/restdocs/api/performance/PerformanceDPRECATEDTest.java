package org.sopt.confeti.restdocs.api.performance;

import static io.restassured.RestAssured.given;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.sopt.confeti.restdocs.base.APIBaseTest;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;

public class PerformanceDPRECATEDTest extends APIBaseTest {

    private static final String TAG = "performance";

    @Test
    @DisplayName("공연 연관 검색 API 테스트")
    void 공연_연관_검색_API() {
        given(this.spec)
                .filter(APIDocument(
                        tag(TAG)
                                .queryParameters(
                                        parameterWithName("accessToken").description("엑세스 토큰").optional(),
                                        parameterWithName("term").description("검색어"),
                                        parameterWithName("limit").description(
                                                        detail(
                                                                "검색 개수",
                                                                "범위 : 1 ~ 10",
                                                                "기본 값 : 1"
                                                        )
                                                )
                                                .optional(),
                                        parameterWithName("status").description(
                                                detail(
                                                        "공연 진행 상태",
                                                        "값 : all | expected"
                                                )
                                        ).optional()
                                )
                                .responseFields(
                                        fieldWithPath("status").type(JsonFieldType.NUMBER).description("200"),
                                        fieldWithPath("message").type(JsonFieldType.STRING).description("요청이 성공했습니다."),
                                        fieldWithPath("data.performances").type(JsonFieldType.ARRAY)
                                                .description("공연 리스트"),
                                        fieldWithPath("data.performances[].id").type(JsonFieldType.NUMBER)
                                                .description("공연 아이디"),
                                        fieldWithPath("data.performances[].title").type(JsonFieldType.STRING)
                                                .description("공연 제목"),
                                        fieldWithPath("data.performances[].posterUrl").type(JsonFieldType.STRING)
                                                .description("공연 포스터 URL")
                                )
                ))
                .when()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .header("Content-Type", "application/json")
                .queryParam("term", "예정된")
                .queryParam("status", "all")
                .get("/performances/search/ac")
                .then()
                .statusCode(200);
    }

    @Override
    protected boolean shouldResetESSearchTermData() {
        return false;
    }

    @Override
    protected boolean shouldResetESPerformanceData() {
        return false;
    }
}
