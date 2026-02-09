package org.sopt.confeti.domain.music.application.dto;

import java.util.HashSet;
import java.util.Set;

public record MusicAPICondition(
    Set<String> ids
) {

    public static MusicAPICondition from(Set<String> ids) {
        return new MusicAPICondition(new HashSet<>(ids));
    }

    public static MusicAPICondition from(String singleId) {
        return new MusicAPICondition(new HashSet<>(Set.of(singleId)));
    }

    public static MusicAPICondition empty() {
        return new MusicAPICondition(new HashSet<>());
    }

    public String extractSingleId() {
        return ids.iterator().next();
    }

    public Set<String> excludeIds(Set<String> excludeIds) {
        Set<String> copiedIds = new HashSet<>(ids);
        copiedIds.removeIf(excludeIds::contains);
        return copiedIds;
    }
}
