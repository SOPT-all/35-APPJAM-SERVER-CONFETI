package org.sopt.confeti.global.config;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import feign.Logger;
import feign.Logger.Level;
import feign.codec.Decoder;
import feign.jackson.JacksonDecoder;
import feign.okhttp.OkHttpClient;
import feign.optionals.OptionalDecoder;
import java.time.Duration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppleMusicFeignConfig {

    @Bean
    public Decoder feignDecoder() {
        ObjectMapper objectMapper =
                new ObjectMapper()
                        // 기본 타입에 null이 할당될 경우 에러 무시
                        .configure(DeserializationFeature.FAIL_ON_NULL_FOR_PRIMITIVES, false)
                        // enum 값이 없는 경우 null 처리
                        .configure(DeserializationFeature.READ_UNKNOWN_ENUM_VALUES_AS_NULL, true)
                        // property가 없을 때 에러 발생 무시
                        .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        return new OptionalDecoder(new JacksonDecoder(objectMapper));
    }

    @Bean
    Logger.Level feignLoggerLevel() {
        return Level.BASIC;
    }

    @Bean
    public OkHttpClient client() {
        return new OkHttpClient(
                new okhttp3.OkHttpClient.Builder()
                        .connectTimeout(Duration.ofSeconds(2))
                        .callTimeout(Duration.ofSeconds(4))
                        .build()
        );
    }
}
