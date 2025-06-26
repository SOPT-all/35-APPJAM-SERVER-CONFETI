package org.sopt.confeti.restdocs.api.user;

import static io.restassured.RestAssured.given;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.sopt.confeti.restdocs.base.APIBaseTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;

public class UserTimetableTest extends APIBaseTest {

    private static final String TAG = "user-timetable";

    @Test
    @DisplayName("타임테이블에 페스티벌 추가 API 테스트")
    void 타임테이블_페스티벌_추가_API() {
        given(this.spec)
                .filter(APIDocument(
                        tag(TAG)
                                .description("타임테이블에 페스티벌 추가 API")
                                .requestHeaders(
                                        headerWithName(HttpHeaders.AUTHORIZATION).description("엑세스 토큰"),
                                        headerWithName(HttpHeaders.CONTENT_TYPE).description("application/json")
                                )
                                .requestFields(
                                        fieldWithPath("festivals").type(JsonFieldType.ARRAY)
                                                .description("추가할 페스티벌 리스트"),
                                        fieldWithPath("festivals[].festivalId").type(JsonFieldType.NUMBER)
                                                .description("페스티벌 아이디")
                                )
                                .responseFields(
                                        fieldWithPath("status").type(JsonFieldType.NUMBER).description("200"),
                                        fieldWithPath("message").type(JsonFieldType.STRING).description("요청이 성공했습니다.")
                                )
                ))
                .when()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .contentType("application/json")
                .header(HttpHeaders.AUTHORIZATION, accessToken)
                .body("{\"festivals\": [{\"festivalId\":  1}, {\"festivalId\" :  2}]}")
                .post("/user/timetables/festivals")
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
