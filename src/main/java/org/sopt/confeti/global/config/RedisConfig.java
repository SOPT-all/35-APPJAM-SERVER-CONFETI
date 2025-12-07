package org.sopt.confeti.global.config;

import io.lettuce.core.ClientOptions;
import io.lettuce.core.ReadFrom;
import io.lettuce.core.SocketOptions;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisSentinelConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class RedisConfig {

    private final RedisInfo redisInfo;

    @Bean
    public LettuceConnectionFactory redisConnectionFactory() {
        log.info("RedisConfig.redisConnectionFactory: Configure connection. command timeout : {}, connect timeout : {}, shutdown timeout : {}", redisInfo.getCommandTimeout(), redisInfo.getConnectTimeout(), redisInfo.getShutdownTimeout());

        RedisSentinelConfiguration sentinelConfig = new RedisSentinelConfiguration()
                .master(redisInfo.getSentinel().getMaster());

        redisInfo.getSentinel().getNodes().forEach(node -> {
            sentinelConfig.sentinel(node.getHost(), node.getPort());
        });

        LettuceClientConfiguration clientConfig = LettuceClientConfiguration.builder()
                .readFrom(ReadFrom.REPLICA_PREFERRED) // replica -> master read
                .commandTimeout(redisInfo.getCommandTimeout())
                .shutdownTimeout(redisInfo.getShutdownTimeout())
                .clientOptions(
                        ClientOptions.builder()
                                .socketOptions(
                                        SocketOptions.builder()
                                                .connectTimeout(redisInfo.getConnectTimeout())
                                                .build()
                                )
                                .build()
                )
                .build();

        LettuceConnectionFactory lettuceConnectionFactory = new LettuceConnectionFactory(sentinelConfig, clientConfig);
        lettuceConnectionFactory.setValidateConnection(true); // 연결 검증

        return lettuceConnectionFactory;
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
}
