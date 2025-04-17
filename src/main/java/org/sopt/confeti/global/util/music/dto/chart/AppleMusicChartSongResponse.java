package org.sopt.confeti.global.util.music.dto.chart;

import java.util.List;
import org.sopt.confeti.global.util.music.dto.music.AppleMusicMusicResponse;

public record AppleMusicChartSongResponse(
        List<AppleMusicMusicResponse> data
) {
}
