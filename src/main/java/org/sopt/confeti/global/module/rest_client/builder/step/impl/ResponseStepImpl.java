package org.sopt.confeti.global.module.rest_client.builder.step.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.sopt.confeti.global.exception.ConfetiException;
import org.sopt.confeti.global.exception.UnauthorizedException;
import org.sopt.confeti.global.message.ErrorMessage;
import org.sopt.confeti.global.module.rest_client.builder.step.ResponseStep;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.client.RestClient;

@Slf4j
@RequiredArgsConstructor
public class ResponseStepImpl implements ResponseStep {

    private final RestClient.ResponseSpec methodType;

    /**
     * 전달받은 클래스 타입으로 변환 후 반환
     *
     * @param responseType
     * @param <T>
     * @return
     */
    @Override
    public <T> T retrieve(Class<T> responseType) {
        try {
            return this.methodType
                    .onStatus(status -> status.equals(HttpStatus.UNAUTHORIZED), (clientRequest, clientResponse) -> {
                        log.error("Token Expired, url : {}", clientRequest.getURI());
                        throw new UnauthorizedException(ErrorMessage.UNAUTHORIZED);
                    })
                    .onStatus(HttpStatusCode::isError, (clientRequest, clientResponse) -> {
                        log.error("RestClient HTTP Error with code : {}, url : {}", clientResponse.getStatusCode(),
                                clientRequest.getURI());
                    })
                    .body(responseType);
        } catch (UnauthorizedException e) {
            throw e;
        } catch (Exception e) {
            log.error("Exception: {} | {}", e.getMessage(), e.getStackTrace()[0].toString());
            throw new ConfetiException(ErrorMessage.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public void retrieve() {
        try {
            this.methodType
                    .onStatus(status -> status.equals(HttpStatus.UNAUTHORIZED), (clientRequest, clientResponse) -> {
                        log.error("Token Expired, url : {}", clientRequest.getURI());
                        throw new UnauthorizedException(ErrorMessage.UNAUTHORIZED);
                    })
                    .onStatus(HttpStatusCode::isError, (clientRequest, clientResponse) -> {
                        log.error("RestClient HTTP Error with code : {}, url : {}", clientResponse.getStatusCode(),
                                clientRequest.getURI());
                    })
                    .body(Void.class);
        } catch (UnauthorizedException e) {
            e.printStackTrace();
            throw e;
        } catch (Exception e) {
//            log.error("Exception: {} | {}", e.getMessage(), e.getStackTrace()[0].toString());
            e.printStackTrace();
            throw new ConfetiException(ErrorMessage.INTERNAL_SERVER_ERROR);
        }
    }
}
