package org.sopt.confeti.global.module.web_client.builder;

import lombok.RequiredArgsConstructor;
import org.sopt.confeti.global.module.web_client.builder.step.MethodStep;
import org.sopt.confeti.global.module.web_client.builder.step.impl.MethodStepImpl;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
@RequiredArgsConstructor
public class ApiWebClientBuilder {

    private final WebClient.Builder webClientBuilder;

    // 체이닝 시작점
    public <T> MethodStep<T> request() {
        return new MethodStepImpl<>(this.webClientBuilder);
    }
}
