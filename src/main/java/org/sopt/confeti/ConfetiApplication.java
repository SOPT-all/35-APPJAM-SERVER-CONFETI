package org.sopt.confeti;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@EnableAspectJAutoProxy
@EnableFeignClients(basePackages = "org.sopt.confeti.external.client")
@SpringBootApplication
public class ConfetiApplication {

    public static void main(String[] args) {
        SpringApplication.run(ConfetiApplication.class, args);
    }
}
