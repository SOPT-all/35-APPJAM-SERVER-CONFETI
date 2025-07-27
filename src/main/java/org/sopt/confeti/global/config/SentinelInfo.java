package org.sopt.confeti.global.config;

import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@NoArgsConstructor
@ConfigurationProperties(prefix = "spring.redis.sentinel")
@Getter
@Setter
@Configuration
public class SentinelInfo {
    private String master;
    private List<SentinelProperty> nodes;
}
