package org.sopt.confeti.global.config;

import java.util.ArrayList;
import java.util.List;
import org.springframework.http.HttpHeaders;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

public class CorsConfig {

    private CorsConfig() {}

    public static CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        //리소스를 허용할 URL 지정
        ArrayList<String> allowedOriginPatterns = new ArrayList<>();
        allowedOriginPatterns.add("*");
        configuration.setAllowedOriginPatterns(allowedOriginPatterns);

        //허용하는 HTTP METHOD 지정
        ArrayList<String> allowedHttpMethods = new ArrayList<>();
        allowedHttpMethods.add("GET");
        allowedHttpMethods.add("POST");
        allowedHttpMethods.add("PUT");
        allowedHttpMethods.add("PATCH");
        allowedHttpMethods.add("DELETE");
        allowedHttpMethods.add("OPTIONS");
        configuration.setAllowedMethods(allowedHttpMethods);

        configuration.setAllowedHeaders(List.of(HttpHeaders.AUTHORIZATION, HttpHeaders.CONTENT_TYPE));

        //인증, 인가를 위한 credentials 를 TRUE로 설정
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);

        return source;
    }
}