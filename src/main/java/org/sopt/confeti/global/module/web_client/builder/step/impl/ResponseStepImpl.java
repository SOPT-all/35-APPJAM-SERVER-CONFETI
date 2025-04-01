package org.sopt.confeti.global.module.web_client.builder.step.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.sopt.confeti.global.exception.ConfetiException;
import org.sopt.confeti.global.message.ErrorMessage;
import org.sopt.confeti.global.module.web_client.builder.step.ResponseStep;

@Slf4j
@RequiredArgsConstructor
public class ResponseStepImpl implements ResponseStep {

    private final Object response;

    /**
     * WebClient의 response를 Object로 리턴<br> 응답값을 response type class로 파싱 후 사용<br>
     * <br>
     * Example)<br> TestDto test = (TestDto) webClientConnectorTest.post(url, path, requestBody) .connectBlock(headers,
     * TestDto.class) .toObjectCall();
     *
     * @return {@link Object}
     */
    @Override
    public Object toObjectCall() {
        return this.response;
    }

    /**
     * WebClient의 response를 JsonNode로 파싱하여 리턴
     *
     * @return {@link JsonNode}
     */
    @Override
    public JsonNode toJsonNodeCall() {
        try {
            ObjectMapper om = new ObjectMapper();

            return om.readTree(this.response.toString());
        } catch (JsonProcessingException e) {
            log.error("JsonProcessingException: {}", e.getMessage());
            throw new ConfetiException(ErrorMessage.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * WebClient의 response를 리턴하지 않음
     */
    @Override
    public void toVoidCall() {
    }
}
