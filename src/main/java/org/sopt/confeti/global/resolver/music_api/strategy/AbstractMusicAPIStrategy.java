package org.sopt.confeti.global.resolver.music_api.strategy;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;

public abstract class AbstractMusicAPIStrategy<T> implements MusicAPIStrategy<T> {

    @Override
    public abstract void collect(
            HashMap<String, Queue<T>> artistMapper,
            Object target
    );

    @Override
    public abstract boolean supports(Class<?> clazz);

    protected void addToMapper(
            HashMap<String, Queue<T>> mapper,
            String targetId,
            T target
    ) {
        if (!mapper.containsKey(targetId)) {
            createQueueToMapper(mapper, targetId);
        }

        mapper.get(targetId).add(target);
    }

    private void createQueueToMapper(
            HashMap<String, Queue<T>> mapper,
            String targetId
    ) {
        mapper.put(targetId, new LinkedList<>());
    }
}
