package org.sopt.confeti.restdocs.api.user;

import static com.epages.restdocs.apispec.RestAssuredRestDocumentationWrapper.document;
import static io.restassured.RestAssured.given;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.sopt.confeti.domain.artist_favorite.ArtistFavorite;
import org.sopt.confeti.restdocs.base.APIBaseTest;
import org.sopt.confeti.restdocs.base.TestDataManager;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;

public class UserFavorite extends APIBaseTest {

    @Test
    @DisplayName("아티스트 좋아요 삭제 API 테스트")
    void 아티스트_좋아요_삭제_API() {
        artistFavoriteRepository.save(ArtistFavorite.create(getUser(), TestDataManager.데이식스));

        given(this.spec)
                .filter(document(DEFAULT_RESTDOC_PATH,
                        requestHeaders(
                                headerWithName(HttpHeaders.AUTHORIZATION).description("엑세스 토큰")
                        ),
                        pathParameters(
                                parameterWithName("artistId").description("아티스트 식별 ID")
                        ),
                        responseFields(
                                fieldWithPath("status").type(JsonFieldType.NUMBER).description("200"),
                                fieldWithPath("message").type(JsonFieldType.STRING).description("요청이 성공했습니다.")
                        )
                ))
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .header(HttpHeaders.AUTHORIZATION, accessToken)
                .pathParams("artistId", TestDataManager.데이식스)
                .delete("/user/favorites/artists/{artistId}")
                .then()
                .statusCode(200);
    }

    @Test
    @DisplayName("아티스트 좋아요 추가 API 테스트")
    void 아티스트_좋아요_추가_API() {
        given(this.spec)
                .filter(document(DEFAULT_RESTDOC_PATH,
                        requestHeaders(
                                headerWithName(HttpHeaders.AUTHORIZATION).description("엑세스 토큰")
                        ),
                        pathParameters(
                                parameterWithName("artistId").description("아티스트 식별 ID")
                        ),
                        responseFields(
                                fieldWithPath("status").type(JsonFieldType.NUMBER).description("200"),
                                fieldWithPath("message").type(JsonFieldType.STRING).description("요청이 성공했습니다.")
                        )
                ))
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .header(HttpHeaders.AUTHORIZATION, accessToken)
                .pathParams("artistId", TestDataManager.데이식스)
                .post("/user/favorites/artists/{artistId}")
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
