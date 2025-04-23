package org.sopt.confeti.api.performance.dto.request;

import java.util.List;

public record PatchRecommendMusics(
        Long performanceId,
        List<PatchRecommendMusic> musicList
) {
    public static PatchRecommendMusics from(PatchRecommendMusics patchRecommendMusics) {
        return new PatchRecommendMusics(
                patchRecommendMusics.performanceId(),
                patchRecommendMusics.musicList.stream()
                        .map(PatchRecommendMusic::from)
                        .toList()
        );
    }
}