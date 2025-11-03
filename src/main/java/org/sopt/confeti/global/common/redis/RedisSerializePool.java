package org.sopt.confeti.global.common.redis;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import lombok.extern.slf4j.Slf4j;
import org.sopt.confeti.global.annotation.RedisSerializable;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public final class RedisSerializePool {

    private static final String SCAN_BASE_PACKAGE = "org.sopt.confeti";

    private final Set<Class<?>> classes = new HashSet<>();

    private RedisSerializePool() {
        register();
        manualRegister();
    }

    private void manualRegister() {
        this
                .add(String.class)
                .add(Integer.class)
                .add(Long.class)
                .add(Object.class);
    }

    private RedisSerializePool add(Class<?> clazz) {
        classes.add(clazz);
        return this;
    }

    private void register() {
        log.info("RedisSerializePool.register: RedisSerializePool configure start.");
        ClassPathScanningCandidateComponentProvider scanner = new ClassPathScanningCandidateComponentProvider(false);
        scanner.addIncludeFilter(new AnnotationTypeFilter(RedisSerializable.class));

        for (BeanDefinition beanDefinition : scanner.findCandidateComponents(SCAN_BASE_PACKAGE)) {
            try {
                Class<?> clazz = Class.forName(beanDefinition.getBeanClassName());
                classes.add(clazz);
                log.info("RedisSerializePool.register: {} class registered in RedisSerializePool.", clazz.getSimpleName());
            } catch (ClassNotFoundException e) {
                log.warn("RedisSerializePool.register: Failed to load class {}.", beanDefinition.getBeanClassName());
            }
        }
    }

    public Set<Class<?>> getPool() {
        return Collections.unmodifiableSet(classes);
    }
}
