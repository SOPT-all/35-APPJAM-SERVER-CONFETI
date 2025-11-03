package org.sopt.confeti.global.config;

import java.time.Duration;
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

    /**
     * Redis command timeout (Seconds)
     */
    private long commandTimeout;

    /**
     * Redis connect timeout (Seconds)
     */
    private long connectTimeout;

    /**
     * Redis shutdown timeout (Milliseconds)
     */
    private long shutdownTimeout;
    private SentinelInfo sentinel;

    public Duration getCommandTimeout() {
        return Duration.ofSeconds(commandTimeout);
    }

    public Duration getConnectTimeout() {
        return Duration.ofSeconds(connectTimeout);
    }

    public Duration getShutdownTimeout() {
        return Duration.ofMillis(shutdownTimeout);
    }
}
