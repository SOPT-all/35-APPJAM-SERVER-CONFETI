package org.sopt.confeti.domain.music.application;

import java.util.ArrayList;
import java.util.List;
import org.sopt.confeti.domain.music.application.dto.CacheResult;
import org.sopt.confeti.domain.music.application.dto.FetchResult;
import org.sopt.confeti.domain.music.application.dto.MusicAPICondition;
import org.sopt.confeti.domain.music.application.dto.PersistResult;

public abstract class MusicAPIService<T> {

    protected abstract void cache(List<T> targetList);

    protected abstract void persist(List<T> targetList);

    protected abstract CacheResult<T> getCached(MusicAPICondition musicAPICondition);

    protected abstract PersistResult<T> getPersisted(MusicAPICondition musicAPICondition);

    protected abstract FetchResult<T> getFetched(MusicAPICondition musicAPICondition);

    public List<T> getList(MusicAPICondition musicAPICondition) {
        List<T> results = new ArrayList<>(musicAPICondition.ids().size());

        CacheResult<T> cacheResult = getCached(musicAPICondition);
        results.addAll(cacheResult.results());
        musicAPICondition = MusicAPICondition.from(
            musicAPICondition.excludeIds(cacheResult.cachedIds()));
        if (isDone(results, musicAPICondition)) {
            return results;
        }

        PersistResult<T> persistResult = getPersisted(musicAPICondition);
        results.addAll(persistResult.results());
        musicAPICondition = MusicAPICondition.from(
            musicAPICondition.excludeIds(persistResult.persistedIds()));
        if (isDone(results, musicAPICondition)) {
            return results;
        }

        FetchResult<T> fetchResult = getFetched(musicAPICondition);
        results.addAll(fetchResult.results());

        return results;
    }

    private boolean isDone(List<T> results, MusicAPICondition musicAPICondition) {
        return !results.isEmpty() && musicAPICondition.ids().isEmpty();
    }
}
