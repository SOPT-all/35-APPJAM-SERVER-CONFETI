package org.sopt.confeti.global.resolver.music_api.strategy;

import java.util.HashMap;
import java.util.Queue;

public interface MusicAPIStrategy<T> {

    void collect(
            HashMap<String, Queue<T>> artistMapper,
            Object target
    );

    boolean supports(Class<?> clazz);
}
