package org.sopt.confeti.global.module.rest_client.builder.step.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.sopt.confeti.global.exception.ConfetiException;
import org.sopt.confeti.global.message.ErrorMessage;
import org.sopt.confeti.global.module.rest_client.builder.step.ResponseStep;
import org.springframework.web.client.RestClient;

@Slf4j
@RequiredArgsConstructor
public class ResponseStepImpl implements ResponseStep {

    private final RestClient.ResponseSpec methodType;

    /**
     * 전달받은 클래스 타입으로 변환 후 반환
     * @param responseType
     * @return
     * @param <T>
     */
    @Override
    public <T> T retrieve(Class<T> responseType) {
        try {
            return this.methodType
                    .body(responseType);
        } catch (Exception e) {
            log.error("Exception: {} | {}", e.getMessage(), e.getStackTrace()[0].toString());
            throw new ConfetiException(ErrorMessage.INTERNAL_SERVER_ERROR);
        }
    }
}
