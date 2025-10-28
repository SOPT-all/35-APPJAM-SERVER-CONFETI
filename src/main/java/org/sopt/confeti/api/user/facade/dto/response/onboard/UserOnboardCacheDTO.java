package org.sopt.confeti.api.user.facade.dto.response.onboard;

import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;
import org.sopt.confeti.api.user.facade.dto.response.UserOnboardTopArtistDTO;
import org.sopt.confeti.api.user.facade.dto.response.UserOnboardTopArtistsDTO;

public record UserOnboardCacheDTO(
    Set<String> favoriteArtistIds,
    Set<String> exposedArtistIds
) {

    public static UserOnboardCacheDTO createWithExposedArtistIds(Set<String> exposedArtistIds) {
        return new UserOnboardCacheDTO(Collections.emptySet(), exposedArtistIds);
    }

    public static UserOnboardCacheDTO from(UserOnboardTopArtistsDTO topArtists) {
        return createWithExposedArtistIds(
            topArtists.artists().stream()
                .map(UserOnboardTopArtistDTO::id)
                .collect(Collectors.toSet())
        );
    }

    public static UserOnboardCacheDTO of(
        Set<String> favoriteArtistIds,
        Set<String> exposedArtistIds
    ) {
        return new UserOnboardCacheDTO(favoriteArtistIds, exposedArtistIds);
    }

    public static UserOnboardCacheDTO empty() {
        return new UserOnboardCacheDTO(Collections.emptySet(), Collections.emptySet());
    }

    public static UserOnboardCacheDTO createWithFavoriteArtistIds(Set<String> favoriteArtistIds) {
        return new UserOnboardCacheDTO(favoriteArtistIds, Collections.emptySet());
    }
}
