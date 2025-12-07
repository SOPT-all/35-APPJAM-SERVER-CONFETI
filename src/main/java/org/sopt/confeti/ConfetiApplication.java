package org.sopt.confeti;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.elasticsearch.ReactiveElasticsearchRepositoriesAutoConfiguration;
import org.springframework.boot.autoconfigure.data.redis.RedisRepositoriesAutoConfiguration;

@SpringBootApplication(
    exclude = {
        ReactiveElasticsearchRepositoriesAutoConfiguration.class,
        RedisRepositoriesAutoConfiguration.class
    }
)
public class ConfetiApplication {

    public static void main(String[] args) {
        SpringApplication.run(ConfetiApplication.class, args);
    }
}
