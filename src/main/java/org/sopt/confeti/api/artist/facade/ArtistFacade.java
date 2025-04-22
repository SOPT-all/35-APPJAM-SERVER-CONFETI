package org.sopt.confeti.api.artist.facade;

import java.util.Objects;
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

    private static final int ARTIST_SEARCH_COUNT = 1;

    private final ArtistFavoriteService artistFavoriteService;
    private final MusicAPIHandler musicAPIHandler;

    @Transactional(readOnly = true)
    public SearchArtistDTO search(Long userId, String term, String aid) {
        Optional<ConfetiArtist> artist = Optional.empty();
        boolean isFavorite = false;

        if (isPresent(aid)) {
            artist = musicAPIHandler.findArtistByArtistId(aid);
        }

        if (isPresent(term)) {
            artist = musicAPIHandler.findArtistsByKeyword(term, ARTIST_SEARCH_COUNT).stream()
                    .findFirst();
        }

        if (isPresent(userId) && artist.isPresent()) {
            isFavorite = artistFavoriteService.isFavorite(userId, artist.get().getId());
        }

        return SearchArtistDTO.from(
                artist.orElse(ConfetiArtist.empty()),
                isFavorite
        );
    }

    public SearchACArtistsDTO searchACArtists(String term, int limit) {
        return SearchACArtistsDTO.from(
                musicAPIHandler.findArtistsByKeyword(term, limit)
        );
    }

    private boolean isPresent(Object target) {
        return Objects.nonNull(target);
    }
}
