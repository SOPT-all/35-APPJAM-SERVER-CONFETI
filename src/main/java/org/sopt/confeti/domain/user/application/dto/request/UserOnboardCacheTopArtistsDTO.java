package org.sopt.confeti.domain.user.application.dto.request;

import java.util.Set;
import java.util.stream.Collectors;
import org.sopt.confeti.api.user.facade.dto.response.UserOnboardTopArtistDTO;
import org.sopt.confeti.api.user.facade.dto.response.UserOnboardTopArtistsDTO;

public record UserOnboardCacheTopArtistsDTO(
        Set<String> artistIds
) {
    public static UserOnboardCacheTopArtistsDTO from(UserOnboardTopArtistsDTO artistsDTO) {
        return new UserOnboardCacheTopArtistsDTO(
                artistsDTO.artists().stream()
                        .map(UserOnboardTopArtistDTO::id)
                        .collect(Collectors.toSet())
        );
    }

    public static UserOnboardCacheTopArtistsDTO from(Set<String> artistIds) {
        return new UserOnboardCacheTopArtistsDTO(artistIds);
    }
}
