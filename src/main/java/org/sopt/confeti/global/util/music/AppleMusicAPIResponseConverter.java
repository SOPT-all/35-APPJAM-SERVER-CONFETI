package org.sopt.confeti.global.util.music;

import java.util.List;
import java.util.Optional;
import org.sopt.confeti.global.resolver.music_api.artist.vo.ConfetiArtist;
import org.sopt.confeti.global.resolver.music_api.song.vo.ConfetiSong;
import org.sopt.confeti.global.util.music.dto.artist.AppleMusicArtistResponse;
import org.sopt.confeti.global.util.music.dto.artist.AppleMusicArtistsResponse;
import org.sopt.confeti.global.util.music.dto.chart.AppleMusicChartResponse;
import org.sopt.confeti.global.util.music.dto.chart.AppleMusicChartSongResponse;
import org.sopt.confeti.global.util.music.dto.chart.AppleMusicChartsResponse;
import org.sopt.confeti.global.util.music.dto.music.AppleMusicArtistMusicsResponse;
import org.sopt.confeti.global.util.music.dto.music.AppleMusicMusicsResponse;
import org.sopt.confeti.global.util.music.dto.music.SongPage;
import org.sopt.confeti.global.util.music.dto.search.AppleMusicSearchResponse;
import org.sopt.confeti.global.util.music.dto.search.AppleMusicSearchResultsResponse;
import org.springframework.stereotype.Component;

@Component
public class AppleMusicAPIResponseConverter {

    public List<ConfetiArtist> convertToConfetiArtists(final AppleMusicArtistsResponse artists) {
        return Optional.ofNullable(artists)
            .map(AppleMusicArtistsResponse::data)
            .map(data ->
                data.stream()
                    .map(ConfetiArtist::from)
                    .toList()
            ).orElseGet(List::of);
    }

    public List<ConfetiArtist> convertToConfetiArtists(
        final AppleMusicSearchResponse searchResult) {
        return Optional.ofNullable(searchResult)
            .map(AppleMusicSearchResponse::results)
            .map(AppleMusicSearchResultsResponse::artists)
            .map(this::convertToConfetiArtists)
            .orElseGet(List::of);
    }

    public Optional<ConfetiArtist> convertToConfetiArtist(final AppleMusicArtistResponse artist) {
        return Optional.of(
            Optional.ofNullable(artist)
                .map(ConfetiArtist::from)
                .orElse(ConfetiArtist.empty())
        );
    }

    public List<ConfetiSong> convertToConfetiMusics(final AppleMusicMusicsResponse musics) {
        return Optional.ofNullable(musics)
            .map(AppleMusicMusicsResponse::data)
            .map(data ->
                data.stream()
                    .map(ConfetiSong::from)
                    .toList()
            ).orElseGet(List::of);
    }

    public List<ConfetiSong> convertToConfetiMusics(final AppleMusicChartsResponse charts) {
        return Optional.ofNullable(charts)
            .map(AppleMusicChartsResponse::results)
            .map(AppleMusicChartResponse::songs)
            .map(songs ->
                songs.stream()
                    .findFirst()
                    .map(AppleMusicChartSongResponse::data)
                    .orElseGet(List::of)
            )
            .map(musics ->
                musics.stream()
                    .map(ConfetiSong::from)
                    .toList()
            ).orElseGet(List::of);
    }

    public SongPage convertToConfetiMusicPage(AppleMusicArtistMusicsResponse artistMusics) {
        return Optional.ofNullable(artistMusics)
            .map(musics -> SongPage.of(
                musics.next(),
                Optional.ofNullable(musics.data())
                    .map(data ->
                        data.stream()
                            .map(ConfetiSong::from)
                            .toList()
                    ).orElseGet(List::of)
            )).orElseGet(SongPage::empty);
    }

    public SongPage convertToConfetiMusicPage(AppleMusicSearchResponse searchResult) {
        return Optional.ofNullable(searchResult)
            .map(AppleMusicSearchResponse::results)
            .map(AppleMusicSearchResultsResponse::songs)
            .map(musics -> SongPage.of(
                musics.next(),
                Optional.ofNullable(musics.data())
                    .map(data ->
                        data.stream()
                            .map(ConfetiSong::from)
                            .toList()
                    ).orElseGet(List::of)
            )).orElseGet(SongPage::empty);
    }
}
