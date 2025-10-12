package org.sopt.confeti.global.config;

import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@Configuration
@EnableAspectJAutoProxy
@EnableJpaAuditing
@EnableFeignClients(basePackages = "org.sopt.confeti.external.client")
public class AppConfig {
}
