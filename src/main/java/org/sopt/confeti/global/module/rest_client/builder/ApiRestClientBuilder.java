package org.sopt.confeti.global.module.rest_client.builder;

import lombok.RequiredArgsConstructor;
import org.sopt.confeti.global.module.rest_client.builder.step.MethodStep;
import org.sopt.confeti.global.module.rest_client.builder.step.impl.MethodStepImpl;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
@RequiredArgsConstructor
public class ApiRestClientBuilder {

    private final RestClient.Builder restClientBuilder;

    // 체이닝 시작점
    public <T> MethodStep<T> request() {
        return new MethodStepImpl<>(this.restClientBuilder);
    }
}
