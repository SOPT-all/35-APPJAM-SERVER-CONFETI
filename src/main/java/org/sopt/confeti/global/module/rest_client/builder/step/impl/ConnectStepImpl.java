package org.sopt.confeti.global.module.rest_client.builder.step.impl;

import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.sopt.confeti.global.exception.ConfetiException;
import org.sopt.confeti.global.message.ErrorMessage;
import org.sopt.confeti.global.module.rest_client.builder.step.ConnectStep;
import org.sopt.confeti.global.module.rest_client.builder.step.ResponseStep;
import org.springframework.web.client.RestClient;

@Slf4j
@RequiredArgsConstructor
public class ConnectStepImpl implements ConnectStep {

    private final RestClient.RequestHeadersSpec<?> methodType;

    /**
     * RestClient header 설정 없이 요청 전송
     * @return 다음 ResponseStep 반환
     */
    @Override
    public ResponseStep connect() {
        try {
            return new ResponseStepImpl(
                    this.methodType
                            .retrieve()
            );
        } catch (Exception e) {
            log.error("Exception: {} | {}", e.getMessage(), e.getStackTrace()[0].toString());
            throw new ConfetiException(ErrorMessage.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * RestClient header 설정 후 요청 전송
     * @param headers
     * @return 다음 ResponseStep 반환
     */
    @Override
    public ResponseStep connect(Map<String, String> headers) {
        try {
            return new ResponseStepImpl(
                    this.methodType
                            .headers(httpHeaders -> httpHeaders.setAll(
                                    headers == null || headers.isEmpty() ? new HashMap<>() : headers))
                            .retrieve()
            );
        } catch (Exception e) {
            log.error("Exception: {} | {}", e.getMessage(), e.getStackTrace()[0].toString());
            throw new ConfetiException(ErrorMessage.INTERNAL_SERVER_ERROR);
        }
    }
}
