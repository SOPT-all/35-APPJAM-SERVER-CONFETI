package org.sopt.confeti.api.performance.dto.request;

public record PatchRecommendMusic(
        String musicId
) {
    public static PatchRecommendMusic from(PatchRecommendMusic music) {
        return new PatchRecommendMusic(music.musicId());
    }
}