package org.sopt.confeti.global.config;

import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableAspectJAutoProxy
@EnableJpaAuditing
@EnableFeignClients(basePackages = "org.sopt.confeti.external.client")
@EnableJpaRepositories(basePackages = "org.sopt.confeti.domain")
public class AppConfig {

}
