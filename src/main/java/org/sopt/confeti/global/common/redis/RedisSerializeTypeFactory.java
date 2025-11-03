package org.sopt.confeti.global.common.redis;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeBase;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.springframework.stereotype.Component;

@Component
public class RedisSerializeTypeFactory {

    private final Map<CompositeKey, TypeBase> typeMapper = new HashMap<>();

    private record CompositeKey(
            Class<?> firstKey,
            Class<?> secondKey
    ) {}

    public RedisSerializeTypeFactory(RedisSerializePool serializePool,
                                     ObjectMapper objectMapper) {
        serializePool.getPool().forEach(clazz -> {
            // Set
            typeMapper.put(
                    new CompositeKey(Set.class, clazz),
                    objectMapper.getTypeFactory().constructCollectionType(Set.class, clazz)
            );

            // List
            typeMapper.put(
                    new CompositeKey(List.class, clazz),
                    objectMapper.getTypeFactory().constructCollectionType(List.class, clazz)
            );
        });
    }

    public TypeBase getSerializeType(Class<?> firstType, Class<?> secondType) {
        return typeMapper.get(new CompositeKey(firstType, secondType));
    }
}
