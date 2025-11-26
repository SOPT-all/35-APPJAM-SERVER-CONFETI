package org.sopt.confeti.domain.music.application.dto;

import java.util.List;

public record FetchResult<T>(
        List<T> results
) {
}
