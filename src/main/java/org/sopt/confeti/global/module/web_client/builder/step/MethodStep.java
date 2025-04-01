package org.sopt.confeti.global.module.web_client.builder.step;

import org.springframework.util.MultiValueMap;

public interface MethodStep<T> {
    PostRequestBuilder post();

    interface PostRequestBuilder {
        PostRequestBuilder baseUrl(String baseUrl);

        PostRequestBuilder path(String path);

        PostRequestBuilder params(MultiValueMap<String, String> params);

        <T> BodySpec<T> body(T requestBody);

        ConnectStep build();
    }

    // body 설정 이후 사용
    interface BodySpec<T> {
        ConnectStep build();
    }

    GetRequestBuilder get();

    interface GetRequestBuilder {
        GetRequestBuilder baseUrl(String baseUrl);

        GetRequestBuilder path(String path);

        GetRequestBuilder params(MultiValueMap<String, String> params);

        ConnectStep build();
    }
}
