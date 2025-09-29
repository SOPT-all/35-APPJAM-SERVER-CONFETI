package org.sopt.confeti.global.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .components(new Components())
                .info(
                        new Info()
                                .title("CONFETI Server API Document")
                                .version("v1.0.0")
                                .description("서버 API Swagger 명세서입니다.")
                );
    }

    /** 아티스트 그룹 */
    @Bean
    public GroupedOpenApi groupArtist() {
        return GroupedOpenApi.builder()
                .group("artist")
                .pathsToMatch("/artists/**")
                .build();
    }

    /** 어드민 그룹 */
    @Bean
    public GroupedOpenApi groupAdmin() {
        return GroupedOpenApi.builder()
                .group("admin")
                .pathsToMatch("/admin/**")
                .build();
    }

    /** 공연 그룹 */
    @Bean
    public GroupedOpenApi groupPerformance() {
        return GroupedOpenApi.builder()
                .group("performance")
                .pathsToMatch("/performances/**")
                .build();
    }

    /** 유저 그룹 */
    @Bean
    public GroupedOpenApi groupUser() {
        return GroupedOpenApi.builder()
                .group("user")
                .pathsToMatch("/user/**")
                .pathsToExclude("/user/onboard/**", "/user/timetables/**")
                .build();
    }

    /** 인증 그룹 */
    @Bean
    public GroupedOpenApi groupAuth() {
        return GroupedOpenApi.builder()
                .group("auth")
                .pathsToMatch("/auth/**", "/user/onboard/**")
                .build();
    }

    /** 셋리스트 그룹 */
    @Bean
    public GroupedOpenApi groupSetlist() {
        return GroupedOpenApi.builder()
                .group("setlist")
                .pathsToMatch("/my/setlists/**")
                .build();
    }

    /** 유저 타임테이블 그룹 */
    @Bean
    public GroupedOpenApi groupUserTimetable() {
        return GroupedOpenApi.builder()
                .group("user timetable")
                .pathsToMatch("/user/timetables/**")
                .build();
    }
}
