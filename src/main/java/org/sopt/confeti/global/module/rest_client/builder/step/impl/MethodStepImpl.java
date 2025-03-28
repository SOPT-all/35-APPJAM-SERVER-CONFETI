package org.sopt.confeti.global.module.rest_client.builder.step.impl;

import java.net.URI;
import java.util.function.Function;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.hc.client5.http.classic.HttpClient;
import org.apache.hc.client5.http.config.RequestConfig;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.util.Timeout;
import org.sopt.confeti.global.module.rest_client.builder.step.ConnectStep;
import org.sopt.confeti.global.module.rest_client.builder.step.MethodStep;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestClient;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.util.DefaultUriBuilderFactory;
import org.springframework.web.util.DefaultUriBuilderFactory.EncodingMode;
import org.springframework.web.util.UriBuilder;

@Slf4j
@RequiredArgsConstructor
public class MethodStepImpl<T> implements MethodStep<T> {

    private final RestClient.Builder restClientBuilder;
    private RestClient.RequestHeadersSpec<?> methodType;

    /**
     * RestClient의 baseUrl과 defaultHeader, encoding 설정
     * @param baseUrl
     * @return
     */
    private RestClient setBaseUrl(String baseUrl) {
        // 인코딩 설정
        DefaultUriBuilderFactory factory = new DefaultUriBuilderFactory();
        factory.setEncodingMode(EncodingMode.VALUES_ONLY);

        // memory size 설정
        ExchangeStrategies exchangeStrategies = ExchangeStrategies.builder()
                .codecs(configurer -> configurer.defaultCodecs().maxInMemorySize(50 * 1024 * 1024))
                .build();

        // HttpClient 생성 (타임아웃 설정)
        HttpClient httpClient = HttpClients.custom()
                .setDefaultRequestConfig(
                        RequestConfig.custom()
                                .setConnectionRequestTimeout(Timeout.ofSeconds(120))
                                .setResponseTimeout(Timeout.ofSeconds(120))
                                .build()
                )
                .build();

        return this.restClientBuilder
                .requestFactory(new HttpComponentsClientHttpRequestFactory(httpClient))
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

            RestClient restClient = setBaseUrl(baseUrl);

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

            // GET 요청 생성
            methodType = restClient
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
        public PostRequestBuilder baseUrl(String baseUrl) {
            this.baseUrl = baseUrl;
            return this;
        }

        @Override
        public PostRequestBuilder path(String path) {
            this.path = path;
            return this;
        }

        @Override
        public PostRequestBuilder params(MultiValueMap<String, String> params) {
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

            RestClient restClient = setBaseUrl(baseUrl);

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

            RestClient.RequestBodySpec requestBodySpec = restClient
                    .post()
                    .uri(uriFunction);

            RestClient.RequestHeadersSpec<?> requestHeadersSpec = requestBodySpec;

            if (hasBody) {
                requestHeadersSpec = requestBodySpec.body(requestBody);
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
