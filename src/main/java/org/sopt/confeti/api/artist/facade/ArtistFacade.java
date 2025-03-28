package org.sopt.confeti.api.artist.facade;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.sopt.confeti.global.annotation.Facade;
import org.sopt.confeti.api.artist.facade.dto.response.SearchArtistDTO;
import org.sopt.confeti.domain.artist_favorite.application.ArtistFavoriteService;
import org.sopt.confeti.global.resolver.artist.vo.ConfetiArtist;
import org.sopt.confeti.global.util.music.MusicAPIHandler;
import org.springframework.transaction.annotation.Transactional;

@Facade
@RequiredArgsConstructor
public class ArtistFacade {

    private final ArtistFavoriteService artistFavoriteService;
    private final MusicAPIHandler musicAPIHandler;

    @Transactional(readOnly = true)
    public SearchArtistDTO searchByKeyword(final Long userId, final String keyword) {
        Optional<ConfetiArtist> confetiArtist = musicAPIHandler.findArtistByKeyword(keyword);

        boolean isFavorite = false;

        if (userId != null && confetiArtist.isPresent()) {
            isFavorite = artistFavoriteService.isFavorite(userId, confetiArtist.get().getArtistId());
        }

        return SearchArtistDTO.from(
                confetiArtist.orElse(ConfetiArtist.empty()),
                isFavorite
        );
    }
}
