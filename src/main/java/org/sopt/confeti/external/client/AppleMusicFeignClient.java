package org.sopt.confeti.external.client;

import org.sopt.confeti.global.config.AppleMusicFeignConfig;
import org.sopt.confeti.global.util.music.dto.artist.AppleMusicArtistResponse;
import org.sopt.confeti.global.util.music.dto.artist.AppleMusicArtistsResponse;
import org.sopt.confeti.global.util.music.dto.chart.AppleMusicChartsResponse;
import org.sopt.confeti.global.util.music.dto.music.AppleMusicArtistMusicsResponse;
import org.sopt.confeti.global.util.music.dto.music.AppleMusicMusicsResponse;
import org.sopt.confeti.global.util.music.dto.search.AppleMusicSearchResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(
        name = "AppleMusicFeignClient",
        url = "${apple-music.api.host}",
        path = "${apple-music.api.path}",
        configuration = AppleMusicFeignConfig.class
)
public interface AppleMusicFeignClient {

    @GetMapping("/artists/{id}")
    AppleMusicArtistResponse getArtistById(
            @PathVariable String id
    );

    @GetMapping("/artists")
    AppleMusicArtistsResponse getArtists(
            @RequestParam String ids
    );

    @GetMapping("/artists/{id}/view/{view}")
    @Deprecated
    AppleMusicArtistsResponse getRelatedArtistsById(
            @PathVariable String id,
            @PathVariable String view,
            @RequestParam String limit
    );

    @GetMapping("/artists/{id}/view/similar-artists")
    AppleMusicArtistsResponse getRelatedArtistsById(
            @PathVariable String id,
            @RequestParam String limit
    );

    @GetMapping("/artists/{id}/view/top-songs")
    AppleMusicMusicsResponse getArtistTopSongsById(
            @PathVariable String id,
            @RequestParam String limit
    );

    @GetMapping("/artists/{id}/songs")
    AppleMusicArtistMusicsResponse getArtistSongsById(
            @PathVariable String id,
            @RequestParam String limit,
            @RequestParam String offset
    );

    @GetMapping("/songs")
    AppleMusicMusicsResponse getSongsByIds(
            @RequestParam String ids
    );

    @GetMapping("/search")
    AppleMusicSearchResponse searchByKeyword(
            @RequestParam String term,
            @RequestParam String types,
            @RequestParam String limit,
            @RequestParam(required = false) String offset,
            @RequestParam(required = false) String with
    );

    @GetMapping("/charts")
    AppleMusicChartsResponse getCharts(
            @RequestParam String types,
            @RequestParam String limit
    );
}
