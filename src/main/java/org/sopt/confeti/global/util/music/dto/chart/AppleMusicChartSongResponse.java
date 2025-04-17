package org.sopt.confeti.global.util.music.dto.chart;

import java.util.List;
import org.sopt.confeti.global.util.music.dto.song.AppleMusicSongResponse;

public record AppleMusicChartSongResponse(
        List<AppleMusicSongResponse> data
) {
}
