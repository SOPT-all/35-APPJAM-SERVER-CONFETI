package org.sopt.confeti.global.module.web_client.builder.step.impl;

import io.netty.channel.ChannelOption;
import java.time.Duration;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.sopt.confeti.global.module.web_client.builder.step.ConnectStep;
import org.sopt.confeti.global.module.web_client.builder.step.MethodStep;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.DefaultUriBuilderFactory;
import reactor.netty.http.client.HttpClient;

@Slf4j
@RequiredArgsConstructor
public class MethodStepImpl<T> implements MethodStep<T> {

    private final WebClient.Builder webClientBuilder;
    private WebClient.RequestHeadersSpec<?> methodType;

    /**
     * WebClient의 baseUrl과 defaultHeader, encoding 설정
     *
     * @return {@link WebClient}
     */
    private WebClient setBaseUrl(String baseUrl) {
        // 인코딩 설정
        DefaultUriBuilderFactory factory = new DefaultUriBuilderFactory(baseUrl);
        factory.setEncodingMode(DefaultUriBuilderFactory.EncodingMode.VALUES_ONLY);

        // memory size 설정
        ExchangeStrategies exchangeStrategies = ExchangeStrategies.builder()
                .codecs(configurer -> configurer.defaultCodecs().maxInMemorySize(50 * 1024 * 1024)) // to unlimited memory size
                .build();

        // timeout 설정
        ReactorClientHttpConnector httpConnector = new ReactorClientHttpConnector(
                HttpClient.create()
                        .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 120000)
                        .responseTimeout(Duration.ofSeconds(120)));


        return this.webClientBuilder
                .exchangeStrategies(exchangeStrategies)
                .clientConnector(httpConnector)
                .uriBuilderFactory(factory)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .baseUrl(baseUrl)
                .build();
    }

    /**
     * WebClient의 HttpMethod를 POST로 설정.<br>
     *
     * @return {@link ConnectStep}
     */
    @Override
    public ConnectStep post(String baseUrl, T requestBody) {
        this.methodType = this.setBaseUrl(!StringUtils.hasText(baseUrl) ? "" : baseUrl)
                .post()
                .bodyValue(requestBody);
        return new ConnectStepImpl(this.methodType);
    }

    /**
     * WebClient의 HttpMethod를 POST로 설정.<br>
     * Path 사용
     *
     * @return {@link ConnectStep}
     */
    @Override
    public ConnectStep post(String baseUrl, String path, T requestBody) {
        this.methodType = this.setBaseUrl(!StringUtils.hasText(baseUrl) ? "" : baseUrl)
                .post()
                .uri(uriBuilder -> uriBuilder
                        .path(path)
                        .build())
                .bodyValue(requestBody);
        return new ConnectStepImpl(this.methodType);
    }

    /**
     * WebClient의 HttpMethod를 POST로 설정.<br>
     * queryParam 사용
     *
     * @return {@link ConnectStep}
     */
    @Override
    public ConnectStep post(String baseUrl, MultiValueMap<String, String> params, T requestBody) {
        this.methodType = this.setBaseUrl(baseUrl)
                .post()
                .uri(uriBuilder -> uriBuilder
                        .queryParams(params)
                        .build())
                .bodyValue(requestBody);
        return new ConnectStepImpl(this.methodType);
    }

    /**
     * WebClient의 HttpMethod를 GET으로 설정.<br>
     *
     * @return {@link ConnectStep}
     */
    @Override
    public ConnectStep get(String baseUrl, MultiValueMap<String, String> params) {
        this.methodType = this.setBaseUrl(baseUrl)
                .get()
                .uri(uriBuilder -> uriBuilder
                        .queryParams(params == null || params.isEmpty() ? new LinkedMultiValueMap<>() : params)
                        .build());
        return new ConnectStepImpl(this.methodType);
    }

    @Override
    public ConnectStep get(String baseUrl, String path, MultiValueMap<String, String> params) {
        this.methodType = this.setBaseUrl(baseUrl)
                .get()
                .uri(uriBuilder -> uriBuilder
                        .path(path)
                        .queryParams(params == null || params.isEmpty() ? new LinkedMultiValueMap<>() : params)
                        .build());
        return new ConnectStepImpl(this.methodType);
    }
}
