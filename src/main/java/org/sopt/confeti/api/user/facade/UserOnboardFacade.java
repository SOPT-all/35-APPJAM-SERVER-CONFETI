package org.sopt.confeti.api.user.facade;

import lombok.RequiredArgsConstructor;
import org.sopt.confeti.api.user.facade.dto.response.onboard.UserOnboardRelatedArtistsDTO;
import org.sopt.confeti.global.annotation.Facade;
import org.sopt.confeti.global.util.music.MusicAPIHandler;

@Facade
@RequiredArgsConstructor
public class UserOnboardFacade {

    private final MusicAPIHandler musicAPIHandler;

    public UserOnboardRelatedArtistsDTO getRelatedArtists(String artistId, int limit) {
        return UserOnboardRelatedArtistsDTO.from(
                musicAPIHandler.getRelatedArtists(artistId, limit)
        );
    }
}
