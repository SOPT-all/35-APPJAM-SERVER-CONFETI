package org.sopt.confeti.global.config;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class JacksonConfig {

    @Bean
    @Primary
    public ObjectMapper objectMapper() {
        return new ObjectMapper()
                .registerModule(new JavaTimeModule()) // LocalDateTime 지원
                .registerModule(new ParameterNamesModule()) // record 지원
                .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false) // Date 변환 시 Timestamp 대신 문자열 시간
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false) // 매칭되지 않는 값은 버림 (에러 X)
                .setSerializationInclusion(JsonInclude.Include.NON_NULL);  // null 필드 제외
    }
}
