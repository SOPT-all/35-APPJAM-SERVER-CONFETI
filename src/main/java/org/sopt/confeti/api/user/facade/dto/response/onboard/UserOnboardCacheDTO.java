package org.sopt.confeti.api.user.facade.dto.response.onboard;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Collectors;
import org.sopt.confeti.api.user.facade.dto.response.UserOnboardTopArtistDTO;
import org.sopt.confeti.api.user.facade.dto.response.UserOnboardTopArtistsDTO;

public record UserOnboardCacheDTO(
    @JsonDeserialize(as = LinkedHashSet.class)
    Set<String> favoriteArtistIds,
    Set<String> exposedArtistIds
) {

    public UserOnboardCacheDTO {
        favoriteArtistIds = new LinkedHashSet<>(favoriteArtistIds);
        exposedArtistIds = new HashSet<>(exposedArtistIds);
    }

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

    public UserOnboardCacheDTO withAddFavoriteArtistIds(Collection<String> newFavoriteArtistIds) {
        Set<String> currentFavoriteArtistIds = new LinkedHashSet<>(this.favoriteArtistIds);
        currentFavoriteArtistIds.addAll(newFavoriteArtistIds);
        return new UserOnboardCacheDTO(currentFavoriteArtistIds, this.exposedArtistIds);
    }

    public UserOnboardCacheDTO deleteFavoriteArtistIds(Collection<String> targetArtistIds) {
        Set<String> currentFavoriteArtistIds = new LinkedHashSet<>(this.favoriteArtistIds);
        currentFavoriteArtistIds.removeAll(targetArtistIds);
        return new UserOnboardCacheDTO(currentFavoriteArtistIds, this.exposedArtistIds);
    }
}
