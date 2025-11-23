package org.sopt.confeti.domain.music.application;

import java.util.ArrayList;
import java.util.List;
import org.sopt.confeti.domain.music.application.dto.CacheResult;
import org.sopt.confeti.domain.music.application.dto.FetchResult;
import org.sopt.confeti.domain.music.application.dto.MusicCondition;
import org.sopt.confeti.domain.music.application.dto.PersistResult;

public abstract class MusicService<T> {

    public abstract void cache(List<T> targetList);
    public abstract void persist(List<T> targetList);
    public abstract CacheResult<T> getCached(MusicCondition musicCondition);
    public abstract PersistResult<T> getPersisted(MusicCondition musicCondition);
    public abstract FetchResult<T> getFetched(MusicCondition musicCondition);

    public List<T> getList(MusicCondition musicCondition) {
        List<T> results = new ArrayList<>(musicCondition.ids().size());

        CacheResult<T> cacheResult = getCached(musicCondition);
        results.addAll(cacheResult.results());
        musicCondition.removeIds(cacheResult.cachedIds());
        if (isDone(results, musicCondition)) {
            return results;
        }

        PersistResult<T> persistResult = getPersisted(musicCondition);
        results.addAll(persistResult.results());
        musicCondition.removeIds(persistResult.persistedIds());
        if (isDone(results, musicCondition)) {
            return results;
        }

        FetchResult<T> fetchResult = getFetched(musicCondition);
        results.addAll(fetchResult.results());

        return results;
    }

    private boolean isDone(List<T> results, MusicCondition musicCondition) {
        return !results.isEmpty() && musicCondition.ids().isEmpty();
    }
}
