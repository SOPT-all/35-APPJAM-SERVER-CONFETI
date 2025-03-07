package org.sopt.confeti.domain.auth.jwt;

import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationPropertiesScan(basePackages = "org.sopt.confeti.auth.jwt")
public class JwtPropertiesConfiguration {
}
