package org.sopt.confeti.global.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.lettuce.core.ReadFrom;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisStaticMasterReplicaConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
@RequiredArgsConstructor
@EnableRedisRepositories
public class RedisConfig {

    private final RedisInfo redisInfo;

    @Bean
    public LettuceConnectionFactory redisConnectionFactory() {
        LettuceClientConfiguration clientConfig = LettuceClientConfiguration.builder()
                .readFrom(ReadFrom.REPLICA_PREFERRED) // replica -> master read
                .build();

        // replica 설정
        RedisStaticMasterReplicaConfiguration slaveConfig = new RedisStaticMasterReplicaConfiguration(
                redisInfo.getHost(), redisInfo.getPort()
        );

        // slave 설정 값 추가
        redisInfo.getSlaves().forEach(slave -> slaveConfig.addNode(slave.getHost(), slave.getPort()));
        return new LettuceConnectionFactory(slaveConfig, clientConfig);
    }

    @Bean
    public RedisTemplate<String, Object> redisTemplate(
            ObjectMapper objectMapper
    ) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();

        GenericJackson2JsonRedisSerializer serializer =
                new GenericJackson2JsonRedisSerializer(objectMapper);

        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(serializer);
        template.setConnectionFactory(redisConnectionFactory());
        template.afterPropertiesSet();

        return template;
    }
}
