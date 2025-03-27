package org.sopt.confeti.global.module.web_client;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.sopt.confeti.global.module.web_client.builder.ApiWebClientBuilder;
import org.springframework.boot.actuate.endpoint.web.Link;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class WebClientConnector {
    private final ApiWebClientBuilder webClientBuilder;

    public Mono<String> test1(String from) {
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("from", URLEncoder.encode(from, StandardCharsets.UTF_8));
        return webClientBuilder.request()
                .get("http://localhost:8080", "/test/2", map)
                .connectSubscribe();
    }

    public Mono<String> test2(String from) {
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("from", URLEncoder.encode(from, StandardCharsets.UTF_8));
        return webClientBuilder.request()
                .get("http://localhost:8080", "/test/3", map)
                .connectSubscribe();
    }
}
