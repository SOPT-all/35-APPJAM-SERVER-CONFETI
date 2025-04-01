package org.sopt.confeti.global.module.web_client.builder.step.impl;

import io.netty.channel.ChannelOption;
import java.net.URI;
import java.time.Duration;
import java.util.function.Function;
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
import org.springframework.web.util.DefaultUriBuilderFactory.EncodingMode;
import org.springframework.web.util.UriBuilder;
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
        factory.setEncodingMode(EncodingMode.VALUES_ONLY);

        // memory size 설정
        ExchangeStrategies exchangeStrategies = ExchangeStrategies.builder()
                .codecs(configurer -> configurer.defaultCodecs()
                        .maxInMemorySize(50 * 1024 * 1024)) // to unlimited memory size
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
     * GET 요청 빌더
     */
    @Override
    public GetRequestBuilder get() {
        return new GetRequestBuilderImpl();
    }

    private class GetRequestBuilderImpl implements GetRequestBuilder {
        private String baseUrl;
        private String path;
        private MultiValueMap<String, String> params;

        @Override
        public GetRequestBuilder baseUrl(String baseUrl) {
            this.baseUrl = baseUrl;
            return this;
        }

        @Override
        public GetRequestBuilder path(String path) {
            this.path = path;
            return this;
        }

        @Override
        public GetRequestBuilder params(MultiValueMap<String, String> params) {
            this.params = params;
            return this;
        }

        @Override
        public ConnectStep build() {
            // base url 기본 값
            if (!StringUtils.hasText(baseUrl)) {
                baseUrl = "";
            }

            WebClient webClient = setBaseUrl(baseUrl);

            Function<UriBuilder, URI> uriFunction = uriBuilder -> {
                // path
                if (StringUtils.hasText(path)) {
                    uriBuilder = uriBuilder.path(path);
                }

                // params
                uriBuilder = uriBuilder.queryParams(
                        params == null || params.isEmpty() ? new LinkedMultiValueMap<>() : params
                );

                return uriBuilder.build();
            };

            // Get 요청 생성
            methodType = webClient
                    .get()
                    .uri(uriFunction);
            return new ConnectStepImpl(methodType);
        }
    }

    /**
     * POST 요청 빌더
     */
    @Override
    public PostRequestBuilder post() {
        return new PostRequestBuilderImpl();
    }

    private class PostRequestBuilderImpl implements PostRequestBuilder {
        private String baseUrl;
        private String path;
        private MultiValueMap<String, String> params;
        private Object requestBody;
        private boolean hasBody = false;

        @Override
        public PostRequestBuilderImpl baseUrl(String baseUrl) {
            this.baseUrl = baseUrl;
            return this;
        }

        @Override
        public PostRequestBuilderImpl path(String path) {
            this.path = path;
            return this;
        }

        @Override
        public PostRequestBuilderImpl params(MultiValueMap<String, String> params) {
            this.params = params;
            return this;
        }

        @Override
        public <T> BodySpec<T> body(T requestBody) {
            return new BodySpecImpl<>(this, requestBody);
        }

        @Override
        public ConnectStep build() {
            // base url 기본 값
            if (!StringUtils.hasText(baseUrl)) {
                baseUrl = "";
            }

            WebClient webClient = setBaseUrl(baseUrl);

            Function<UriBuilder, URI> uriFunction = uriBuilder -> {
                // path
                if (StringUtils.hasText(path)) {
                    uriBuilder = uriBuilder.path(path);
                }

                // params
                uriBuilder = uriBuilder.queryParams(
                        params == null || params.isEmpty() ? new LinkedMultiValueMap<>() : params
                );

                return uriBuilder.build();
            };

            // Post 요청 생성
            WebClient.RequestBodySpec requestBodySpec = webClient
                    .post()
                    .uri(uriFunction);

            WebClient.RequestHeadersSpec<?> requestHeadersSpec = requestBodySpec;

            if (hasBody) {
                requestHeadersSpec = requestBodySpec.bodyValue(requestBody);
            }

            methodType = requestHeadersSpec;
            return new ConnectStepImpl(methodType);
        }
    }

    private class BodySpecImpl<R> implements BodySpec<R> {
        private final PostRequestBuilderImpl builder;

        public BodySpecImpl(PostRequestBuilderImpl builder, R requestBody) {
            this.builder = builder;

            this.builder.requestBody = requestBody;
            this.builder.hasBody = true;
        }

        @Override
        public ConnectStep build() {
            return builder.build();
        }
    }
}
