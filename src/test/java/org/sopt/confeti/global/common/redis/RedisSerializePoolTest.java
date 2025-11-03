package org.sopt.confeti.global.common.redis;


import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ExtendWith(MockitoExtension.class)
class RedisSerializePoolTest {

    private static final Logger log = LoggerFactory.getLogger(RedisSerializePoolTest.class);
    @InjectMocks private RedisSerializePool redisSerializePool;

    @Nested
    @DisplayName("RedisKey에 등록된 클래스에 @RedisSerializable 어노테이션이 추가되었는지 테스트")
    class RedisPoolWithRedisSerializable {

        @Test
        @DisplayName("[검증] RedisKey에 등록된 클래스는 @RedisSerializable 어노테이션이 있어야 한다.")
        void validation01() {
            // given
            Set<Class<?>> serializableClasses = new HashSet<>(redisSerializePool.getPool());
            List<? extends Class<?>> registeredTypes = Arrays.stream(RedisKey.values())
                    .map(RedisKey::getType)
                    .toList();

            // then
            for (Class<?> registeredType : registeredTypes)
                assertThat(registeredType).isIn(serializableClasses);
        }
    }

}