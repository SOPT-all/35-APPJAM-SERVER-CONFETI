package org.sopt.confeti.domain.user.application.dto.request;

import java.util.List;
import org.sopt.confeti.api.user.facade.dto.response.UserOnboardTopArtistDTO;
import org.sopt.confeti.api.user.facade.dto.response.UserOnboardTopArtistsDTO;

public record UserOnboardCacheTopArtistsDTO(
        List<String> artistIds
) {
    public static UserOnboardCacheTopArtistsDTO from(UserOnboardTopArtistsDTO artistsDTO) {
        return new UserOnboardCacheTopArtistsDTO(
                artistsDTO.artists().stream()
                        .map(UserOnboardTopArtistDTO::id)
                        .toList()
        );
    }
}
