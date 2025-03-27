package org.sopt.confeti.global.module.web_client.builder.step;

import org.springframework.util.MultiValueMap;

public interface MethodStep<T> {
    ConnectStep post(String baseUrl, T requestBody);
    ConnectStep post(String baseUrl, String path, T requestBody);

    ConnectStep post(String baseUrl, MultiValueMap<String, String> params, T requestBody);

    ConnectStep get(String baseUrl, MultiValueMap<String, String> params);

    ConnectStep get(String baseUrl, String path, MultiValueMap<String, String> params);
}
