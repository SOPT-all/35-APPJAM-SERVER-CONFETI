package org.sopt.confeti.global.module.web_client.builder.step;

import java.util.Map;
import reactor.core.publisher.Mono;

public interface ConnectStep {
    ResponseStep connectBlock(Map<String, String> headers, Class<?> responseType);
    ResponseStep connectBlock();
    Mono<?> connectSubscribe(Map<String, String> headers, Class<?> responseType);
    Mono<String> connectSubscribe();
}
