package org.sopt.confeti.api.artist.facade;

import lombok.RequiredArgsConstructor;
import org.sopt.confeti.api.artist.facade.dto.response.SearchACArtistsDTO;
import org.sopt.confeti.global.annotation.Facade;
import org.sopt.confeti.global.util.music.MusicAPIHandler;

@Facade
@RequiredArgsConstructor
public class ArtistFacade {

    private final MusicAPIHandler musicAPIHandler;

    public SearchACArtistsDTO searchACArtists(String term, int limit) {
        return SearchACArtistsDTO.from(
                musicAPIHandler.findArtistsByKeyword(term, limit)
        );
    }
}
