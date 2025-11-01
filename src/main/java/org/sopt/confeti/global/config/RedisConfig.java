package org.sopt.confeti.global.config;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import io.lettuce.core.ReadFrom;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisSentinelConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Slf4j
@Configuration
@RequiredArgsConstructor
@EnableRedisRepositories
public class RedisConfig {

    private final RedisInfo redisInfo;

    @Bean
    public LettuceConnectionFactory redisConnectionFactory() {
        log.info("RedisConfig.redisConnectionFactory: 연결 설정");
        log.info("RedisConfig.redisConnectionFactory: timeout : {}", redisInfo.getTimeout());

        RedisSentinelConfiguration sentinelConfig = new RedisSentinelConfiguration()
                .master(redisInfo.getSentinel().getMaster());

        redisInfo.getSentinel().getNodes().forEach(node -> {
            sentinelConfig.sentinel(node.getHost(), node.getPort());
        });

        LettuceClientConfiguration clientConfig = LettuceClientConfiguration.builder()
                .readFrom(ReadFrom.REPLICA_PREFERRED) // replica -> master read
                .commandTimeout(redisInfo.getTimeout())
                .build();

        return new LettuceConnectionFactory(sentinelConfig, clientConfig);
    }

    @Bean
    public RedisTemplate<String, String> redisTemplate() {
        RedisTemplate<String, String> template = new RedisTemplate<>();

        template.setKeySerializer(new StringRedisSerializer());
        template.setHashKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new StringRedisSerializer());
        template.setHashValueSerializer(new StringRedisSerializer());
        template.setConnectionFactory(redisConnectionFactory());
        template.afterPropertiesSet();

        return template;
    }

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper()
                .registerModule(new JavaTimeModule()) // LocalDateTime 지원
                .registerModule(new ParameterNamesModule()) // record 지원
                .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false) // Date 변환 시 Timestamp 대신 문자열 시간
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false) // 매칭되지 않는 값은 버림 (에러 X)
                .setSerializationInclusion(JsonInclude.Include.NON_NULL);  // null 필드 제외
    }
}
