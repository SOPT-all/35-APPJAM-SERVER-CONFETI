package org.sopt.confeti.api.artist.facade;

import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.sopt.confeti.api.artist.facade.dto.response.SearchACArtistsDTO;
import org.sopt.confeti.api.artist.facade.dto.response.SearchArtistDTO;
import org.sopt.confeti.domain.artist_favorite.application.ArtistFavoriteService;
import org.sopt.confeti.global.annotation.Facade;
import org.sopt.confeti.global.resolver.music_api.artist.vo.ConfetiArtist;
import org.sopt.confeti.global.util.music.MusicAPIHandler;
import org.springframework.transaction.annotation.Transactional;

@Facade
@RequiredArgsConstructor
public class ArtistFacade {

    private static final int ARTISTS_SEARCH_COUNT = 5;

    private final ArtistFavoriteService artistFavoriteService;
    private final MusicAPIHandler musicAPIHandler;

    @Transactional(readOnly = true)
    public SearchArtistDTO searchByKeyword(final Long userId, final String keyword) {
        List<ConfetiArtist> confetiArtists = musicAPIHandler.findArtistsByKeyword(keyword, ARTISTS_SEARCH_COUNT);
        Optional<ConfetiArtist> confetiArtist = confetiArtists.stream().findFirst();

        boolean isFavorite = false;

        if (userId != null && confetiArtist.isPresent()) {
            isFavorite = artistFavoriteService.isFavorite(userId, confetiArtist.get().getId());
        }

        return SearchArtistDTO.from(
                confetiArtist.orElse(ConfetiArtist.empty()),
                isFavorite
        );
    }

    public SearchACArtistsDTO searchACArtists(String term, int limit) {
        return SearchACArtistsDTO.from(
                musicAPIHandler.findArtistsByKeyword(term, limit)
        );
    }
}
