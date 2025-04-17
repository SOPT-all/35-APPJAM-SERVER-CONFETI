package org.sopt.confeti.global.resolver.music_api;

import java.util.List;
import java.util.Optional;
import org.sopt.confeti.global.resolver.music_api.strategy.MusicAPIStrategy;

public abstract class AbstractMusicAPISpecificResolver implements MusicAPISpecificResolver {

    protected <T> boolean isListType(final T target) {
        return target instanceof List<?>;
    }

    protected <T> boolean isEmpty(T target) {
        return target == null || isListType(target) && ((List<?>) target).isEmpty();
    }

    protected boolean notSupports(MusicAPIStrategy<?> strategy) {
        return strategy == null;
    }

    protected <T> Class<?> getTargetClass(T target) {
        Class<?> targetClass = target.getClass();

        if (isListType(target)) {
            Optional<Class<?>> firstItem = ((List<?>) target).stream()
                    .findFirst()
                    .map(Object::getClass);

            // 값이 비어있을 때는 List 타입
            if (firstItem.isPresent()) {
                targetClass = firstItem.get();
            }
        }

        return targetClass;
    }
}
