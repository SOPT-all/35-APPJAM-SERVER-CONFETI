package org.sopt.confeti.global.module.web_client.builder.step.impl;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.sopt.confeti.global.exception.ConfetiException;
import org.sopt.confeti.global.message.ErrorMessage;
import org.sopt.confeti.global.module.web_client.builder.step.ConnectStep;
import org.sopt.confeti.global.module.web_client.builder.step.ResponseStep;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

@Slf4j
@RequiredArgsConstructor
public class ConnectStepImpl implements ConnectStep {

    private final WebClient.RequestHeadersSpec<?> methodType;
    private Object response;

    /**
     * WebClient의 header와 response class 설정 후 block으로 호출<br>
     * 헤더와 응답값이 존재할 때 사용. (ex. 일반적인 api 호출)<br>
     * 예외처리 포함
     * @return {@link ResponseStep}
     */
    @Override
    public <T> ResponseStep connectBlock(Map<String, String> headers, Class<T> responseType) {
        try {
            this.response = this.methodType
                    .headers(httpHeaders -> httpHeaders.setAll(headers == null || headers.isEmpty() ? new HashMap<>() : headers))
                    .retrieve()
                    .onStatus(HttpStatusCode::isError, clientResponse -> {
                        log.error("WebClient HTTP Error with code : {}, url : {}", clientResponse.statusCode(), clientResponse.request().getURI());
                        return clientResponse.bodyToMono(String.class)
                                .flatMap(msg -> Mono.error(new ConfetiException(ErrorMessage.INTERNAL_SERVER_ERROR)));
                    })
                    .bodyToMono(responseType)
                    .retryWhen(Retry.fixedDelay(3, Duration.ofSeconds(1))
                            .doBeforeRetry(before -> log.info("Retry: {} | {}", before.totalRetries(), before.failure())))
                    .block();
            return new ResponseStepImpl(this.response);
        } catch (Exception e) {
            log.error("Exception: {} | {}", e.getMessage(), e.getStackTrace()[0].toString());
            throw new ConfetiException(ErrorMessage.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * WebClient의 header와 response class 설정 후 block으로 호출<br>
     * 헤더와 응답값이 존재하지 않을 때 사용. (ex. Google chat webhook api 호출)<br>
     * 예외처리 포함
     * @return {@link ResponseStep}
     */
    @Override
    public ResponseStep connectBlock() {
        try {
            this.response = this.methodType
                    .retrieve()
                    .onStatus(HttpStatusCode::isError, clientResponse -> {
                        log.error("WebClient HTTP Error with code : {}, url : {}", clientResponse.statusCode(), clientResponse.request().getURI());
                        return clientResponse.bodyToMono(String.class)
                                .flatMap(msg -> Mono.error(new ConfetiException(ErrorMessage.INTERNAL_SERVER_ERROR)));
                    })
                    .bodyToMono(Object.class)
                    .retryWhen(Retry.fixedDelay(3, Duration.ofSeconds(1))
                            .doBeforeRetry(before -> log.info("Retry: {} | {}", before.totalRetries(), before.failure())))
                    .block();
            return new ResponseStepImpl(this.response);
        } catch (Exception e) {
            log.error("Exception: {} | {}", e.getMessage(), e.getStackTrace()[0].toString());
            throw new ConfetiException(ErrorMessage.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * WebClient의 header와 response class 설정 후 subscribe로 호출<br>
     * 헤더와 응답값이 존재할 때 사용. (ex. 일반적인 api 호출)<br>
     * 예외처리 포함
     * @return {@link ResponseStep}
     */
    @Override
    public <T> Mono<T> connectSubscribe(Map<String, String> headers, Class<T> responseType) {
        try {
            return this.methodType
                    .headers(httpHeaders -> httpHeaders.setAll(headers == null || headers.isEmpty() ? new HashMap<>() : headers))
                    .retrieve()
                    .onStatus(HttpStatusCode::isError, clientResponse -> {
                        log.error("WebClient HTTP Error with code : {}, url : {}", clientResponse.statusCode(), clientResponse.request().getURI());
                        return clientResponse.bodyToMono(String.class)
                                .flatMap(msg -> Mono.error(new ConfetiException(ErrorMessage.INTERNAL_SERVER_ERROR)));
                    })
                    .bodyToMono(responseType)
                    .retryWhen(Retry.fixedDelay(3, Duration.ofSeconds(1))
                            .doBeforeRetry(before -> log.info("Retry: {} | {}", before.totalRetries(), before.failure())));
        } catch (Exception e) {
            throw new ConfetiException(ErrorMessage.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * WebClient의 header와 response class 설정 후 subscribe로 호출<br>
     * 헤더와 응답값이 존재하지 않을 때 사용. (ex. Google chat webhook api 호출)<br>
     * 예외처리 포함
     * @return {@link ResponseStep}
     */
    @Override
    public Mono<String> connectSubscribe() {
        try {
            return this.methodType
                    .retrieve()
                    .onStatus(HttpStatusCode::isError, clientResponse -> {
                        log.error("WebClient HTTP Error with code : {}, url : {}", clientResponse.statusCode(), clientResponse.request().getURI());
                        return clientResponse.bodyToMono(String.class)
                                .flatMap(msg -> Mono.error(new ConfetiException(ErrorMessage.INTERNAL_SERVER_ERROR)));
                    })
                    .bodyToMono(String.class)
                    .retryWhen(Retry.fixedDelay(3, Duration.ofSeconds(1))
                            .doBeforeRetry(before -> log.info("Retry: {} | {}", before.totalRetries(), before.failure())));
        } catch (Exception e) {
            throw new ConfetiException(ErrorMessage.INTERNAL_SERVER_ERROR);
        }
    }
}
