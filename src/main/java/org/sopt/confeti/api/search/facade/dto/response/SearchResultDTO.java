package org.sopt.confeti.api.search.facade.dto.response;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import org.sopt.confeti.domain.view.performance.application.dto.response.PerformanceDTO;
import org.sopt.confeti.global.resolver.music_api.artist.vo.ConfetiArtist;
import org.sopt.confeti.global.resolver.music_api.song.vo.ConfetiSong;

public record SearchResultDTO(
        SearchResultArtistDTO artist,
        List<SearchResultSongDTO> songs,
        List<SearchResultPerformanceDTO> performances
) {
    public static SearchResultDTO of(ConfetiArtist artist, boolean artistFavorite, List<PerformanceDTO> performances,
                                     Map<Long, Boolean> performanceFavorites, List<ConfetiSong> songs) {
        SearchResultArtistDTO artistDTO = null;
        if (Objects.nonNull(artist)) {
            artistDTO = SearchResultArtistDTO.of(artist, artistFavorite);
        }

        return new SearchResultDTO(
                artistDTO,
                songs.stream()
                        .map(SearchResultSongDTO::from)
                        .toList(),
                performances.stream()
                        .map(performance -> SearchResultPerformanceDTO.of(performance,
                                performanceFavorites.get(performance.id())))
                        .toList()
        );
    }

    public static SearchResultDTO of(PerformanceDTO performance, boolean performanceFavorite) {
        return new SearchResultDTO(
                null,
                List.of(),
                List.of(SearchResultPerformanceDTO.of(performance, performanceFavorite))
        );
    }
}
