package org.sopt.confeti.global.util.music.dto.chart;

import java.util.List;

public record AppleMusicChartResponse(
        List<AppleMusicChartSongResponse> songs
) {
}
