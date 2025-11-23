package org.sopt.confeti.domain.music.application.dto;

import java.util.Set;

public record MusicCondition(
        Set<String> ids
) {
    public void removeIds(Set<String> excludeIds) {
        ids.removeIf(excludeIds::contains);
    }
}
