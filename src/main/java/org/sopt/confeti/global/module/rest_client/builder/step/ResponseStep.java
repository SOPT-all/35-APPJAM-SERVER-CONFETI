package org.sopt.confeti.global.module.rest_client.builder.step;

public interface ResponseStep {

    <T> T retrieve(Class<T> responseType);
}
