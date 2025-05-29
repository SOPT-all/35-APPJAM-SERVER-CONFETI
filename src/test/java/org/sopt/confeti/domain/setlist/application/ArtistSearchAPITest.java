package org.sopt.confeti.domain.setlist.application;

import static com.epages.restdocs.apispec.RestAssuredRestDocumentationWrapper.document;
import static io.restassured.RestAssured.given;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.queryParameters;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;

public class ArtistSearchAPITest extends BaseControllerTest {

    @Test
    @DisplayName("아티스트 검색 API 테스트")
    void 아티스트_검색_API_테스트() {
        given(this.spec)
                .filter(document(DEFAULT_RESTDOC_PATH,
                        queryParameters(
                                parameterWithName("accessToken").description("엑세스 토큰").optional(),
                                parameterWithName("term").description("검색어").optional(),
                                parameterWithName("aid").description("아티스트 아이디").optional()
                        ),
                        responseFields(
                                fieldWithPath("status").type(JsonFieldType.NUMBER).description("200"),
                                fieldWithPath("message").type(JsonFieldType.STRING).description("요청이 성공했습니다."),
                                fieldWithPath("data.artist").type(JsonFieldType.OBJECT).description("아티스트 객체"),
                                fieldWithPath("data.artist.artistId").type(JsonFieldType.STRING)
                                        .description("아티스트 아이디"),
                                fieldWithPath("data.artist.name").type(JsonFieldType.STRING).description("아티스트 이름"),
                                fieldWithPath("data.artist.profileUrl").type(JsonFieldType.STRING)
                                        .description("아티스트 프로필 URL"),
                                fieldWithPath("data.artist.recentAlbumName").type(JsonFieldType.STRING)
                                        .description("아티스트 최근 발매 앨범 제목"),
                                fieldWithPath("data.artist.isFavorite").type(JsonFieldType.BOOLEAN)
                                        .description("아티스트 좋아요 여부")
                        )
                ))
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .header("Content-Type", "application/json")
                .when()
                .queryParam("term", "혁오")
                .get("/artists/search")
                .then()
                .statusCode(200);
    }

    @Test
    @DisplayName("공연 연관 검색 API 테스트")
    void 공연_연관_검색_API_테스트() {
        given(this.spec)
                .filter(document(DEFAULT_RESTDOC_PATH,
                        queryParameters(
                                parameterWithName("accessToken").description("엑세스 토큰").optional(),
                                parameterWithName("term").description("검색어"),
                                parameterWithName("limit").description("검색 개수\n범위 : 1 ~ 10\n기본 값 : 1").optional(),
                                parameterWithName("status").description("공연 진행 상태\n값 : all|expected").optional()
                        ),
                        responseFields(
                                fieldWithPath("status").type(JsonFieldType.NUMBER).description("200"),
                                fieldWithPath("message").type(JsonFieldType.STRING).description("요청이 성공했습니다."),
                                fieldWithPath("data.performances").type(JsonFieldType.ARRAY).description("공연 리스트"),
                                fieldWithPath("data.performances[].id").type(JsonFieldType.NUMBER)
                                        .description("공연 아이디"),
                                fieldWithPath("data.performances[].title").type(JsonFieldType.STRING)
                                        .description("공연 제목"),
                                fieldWithPath("data.performances[].posterUrl").type(JsonFieldType.STRING)
                                        .description("공연 포스터 URL")
                        )
                ))
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .header("Content-Type", "application/json")
                .when()
                .queryParam("term", "콘서트")
                .get("/performances/search/ac")
                .then()
                .statusCode(200);
    }

    @Override
    protected boolean shouldResetESSearchTermData() {
        return true;
    }

    @Override
    protected boolean shouldResetESPerformanceData() {
        return false;
    }
}
