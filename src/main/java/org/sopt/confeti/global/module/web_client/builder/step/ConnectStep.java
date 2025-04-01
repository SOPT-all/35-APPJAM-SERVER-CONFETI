package org.sopt.confeti.global.module.web_client.builder.step;

import java.util.Map;
import reactor.core.publisher.Mono;

public interface ConnectStep {
    <T> ResponseStep connectBlock(Map<String, String> headers, Class<T> responseType);

    ResponseStep connectBlock();

    <T> Mono<T> connectSubscribe(Map<String, String> headers, Class<T> responseType);

    Mono<String> connectSubscribe();
}
