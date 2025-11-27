package org.sopt.confeti.global.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import java.util.Arrays;
import java.util.stream.Collectors;
import org.sopt.confeti.domain.user.constant.Role;
import org.sopt.confeti.global.annotation.Permission;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.HandlerMethod;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI openAPI() {
        Info info = new Info()
            .title("CONFETI Server API Document")
            .version("v1.0.0")
            .description("서버 API Swagger 명세서입니다.")
            .contact(
                new Contact().email("confetiserver@gmail.com")
            );

        String jwtScheme = "jwtAuth";
        SecurityRequirement securityRequirement = new SecurityRequirement().addList(jwtScheme);
        Components components = new Components()
            .addSecuritySchemes(jwtScheme, new SecurityScheme()
                .name("Authorization")
                .type(SecurityScheme.Type.HTTP)
                .in(SecurityScheme.In.HEADER)
                .scheme("Bearer")
                .bearerFormat("JWT"));

        return new OpenAPI()
            .addServersItem(new Server().url("http://localhost:8080"))
            .addServersItem(new Server().url("https://api.confeti.xyz"))
            .components(components)
            .info(info)
            .addSecurityItem(securityRequirement);
    }

    /**
     * 아티스트 그룹
     */
    @Bean
    public GroupedOpenApi groupArtist() {
        return GroupedOpenApi.builder()
            .group("artist")
            .pathsToMatch("/artists/**")
            .addOperationCustomizer(this::addPermissionDescription)
            .build();
    }

    /**
     * 어드민 그룹
     */
    @Bean
    public GroupedOpenApi groupAdmin() {
        return GroupedOpenApi.builder()
            .group("admin")
            .pathsToMatch("/admin/**")
            .addOperationCustomizer(this::addPermissionDescription)
            .build();
    }

    /**
     * 공연 그룹
     */
    @Bean
    public GroupedOpenApi groupPerformance() {
        return GroupedOpenApi.builder()
            .group("performance")
            .pathsToMatch(
                "/performances/**",
                "/v4/performances/**"
            )
            .addOperationCustomizer(this::addPermissionDescription)
            .build();
    }

    /**
     * 유저 그룹
     */
    @Bean
    public GroupedOpenApi groupUser() {
        return GroupedOpenApi.builder()
            .group("user")
            .pathsToMatch("/user/**")
            .pathsToExclude("/user/onboard/**", "/user/timetables/**")
            .addOperationCustomizer(this::addPermissionDescription)
            .build();
    }

    /**
     * 인증 그룹
     */
    @Bean
    public GroupedOpenApi groupAuth() {
        return GroupedOpenApi.builder()
            .group("auth")
            .pathsToMatch("/auth/**", "/user/onboard/**")
            .addOperationCustomizer(this::addPermissionDescription)
            .build();
    }

    /**
     * 셋리스트 그룹
     */
    @Bean
    public GroupedOpenApi groupSetlist() {
        return GroupedOpenApi.builder()
            .group("setlist")
            .pathsToMatch(
                "/my/setlists/**",
                "/v4/my/setlists/**"
            )
            .addOperationCustomizer(this::addPermissionDescription)
            .build();
    }

    /**
     * 유저 타임테이블 그룹
     */
    @Bean
    public GroupedOpenApi groupUserTimetable() {
        return GroupedOpenApi.builder()
            .group("user timetable")
            .pathsToMatch(
                "/user/timetables/**",
                "/v4/user/timetables/**"
            )
            .addOperationCustomizer(this::addPermissionDescription)
            .build();
    }

    private Operation addPermissionDescription(Operation operation, HandlerMethod handlerMethod) {
        Permission permission = handlerMethod.getMethodAnnotation(Permission.class);
        if (permission != null) {
            String roles = Arrays.stream(permission.role())
                .map(Role::name)
                .collect(Collectors.joining(", "));

            String currentDesc = operation.getDescription();
            operation.setDescription(
                "🔒 **Required Permissions:** `" + roles + "`\n\n"
                    + (currentDesc != null ? currentDesc : "")
            );
        }
        return operation;
    }
}
