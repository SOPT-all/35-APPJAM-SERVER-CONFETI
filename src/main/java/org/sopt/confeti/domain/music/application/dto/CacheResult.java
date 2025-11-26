package org.sopt.confeti.domain.music.application.dto;

import java.util.List;
import java.util.Set;

public record CacheResult<T>(
        List<T> results,
        Set<String> cachedIds
) {
}
