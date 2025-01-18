package org.sopt.confeti.api.artist.facade;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.sopt.confeti.annotation.Facade;
import org.sopt.confeti.api.artist.facade.dto.response.SearchArtistDTO;
import org.sopt.confeti.domain.artistfavorite.application.ArtistFavoriteService;
import org.sopt.confeti.global.util.artistsearcher.ConfetiArtist;
import org.sopt.confeti.global.util.artistsearcher.SpotifyAPIHandler;
import org.springframework.transaction.annotation.Transactional;

@Facade
@RequiredArgsConstructor
public class ArtistFacade {

    private final ArtistFavoriteService artistFavoriteService;
    private final SpotifyAPIHandler spotifyAPIHandler;

    @Transactional(readOnly = true)
    public SearchArtistDTO searchByKeyword(final Long userId, final String keyword) {
        Optional<ConfetiArtist> confetiArtist = spotifyAPIHandler.findArtistsByKeyword(keyword);

        boolean isFavorite = false;

        if (confetiArtist.isPresent() && userId != null) {
            isFavorite = artistFavoriteService.isFavorite(userId, confetiArtist.get().getArtistId());
        }

        return SearchArtistDTO.from(
                confetiArtist.orElse(ConfetiArtist.empty()),
                isFavorite
        );
    }
}
