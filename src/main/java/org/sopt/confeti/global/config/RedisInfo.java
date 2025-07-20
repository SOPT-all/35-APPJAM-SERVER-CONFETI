package org.sopt.confeti.global.config;

import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@NoArgsConstructor
@Getter
@Setter
@ConfigurationProperties(prefix = "spring.redis")
@Configuration
public class RedisInfo {

    private String host;
    private int port;
    private List<RedisProperty> slaves;
}
