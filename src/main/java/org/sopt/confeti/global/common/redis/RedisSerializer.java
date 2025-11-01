package org.sopt.confeti.global.common.redis;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class RedisSerializer {

    private final RedisSerializeTypeFactory typeFactory;
    private final ObjectMapper objectMapper;

    public <T> String serialize(T obj) {
        try {
            return objectMapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            log.warn("RedisSerializer.serialize : Exception occurred while serialize object : {}", obj);
            return null;
        }
    }

    public <T> T deserialize(String value, Class<T> type) {
        try {
            return objectMapper.readValue(value, type);
        } catch (JsonProcessingException e) {
            log.warn("RedisSerializer.deserialize : Exception occurred while deserialize string to object. value : {}, type : {}", value, type);
            return null;
        }
    }

    public <T> List<T> deserializeToList(String value, Class<T> type) {
        try {
            return objectMapper.readValue(value, typeFactory.getSerializeType(List.class, type));
        } catch (JsonProcessingException e) {
            log.warn("RedisSerializer.deserializeToList : Exception occurred while deserialize string to list. value : {}, type : {}", value, type);
            return null;
        }
    }

    public <T> Set<T> deserializeToSet(String value, Class<T> type) {
        try {
            return objectMapper.readValue(value, typeFactory.getSerializeType(Set.class, type));
        } catch (JsonProcessingException e) {
            log.warn("RedisSerializer.deserializeToSet : Exception occurred while deserialize string to set. value : {}, type : {}", value, type);
            return null;
        }
    }
}
