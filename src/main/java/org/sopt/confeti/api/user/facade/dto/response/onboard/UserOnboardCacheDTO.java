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
import org.sopt.confeti.global.resolver.music_api.artist.vo.ConfetiArtist;

public record UserOnboardCacheDTO(
    @JsonDeserialize(as = LinkedHashSet.class)
    Set<ConfetiArtist> favoriteArtists,
    Set<String> exposedArtistIds
) {

    public UserOnboardCacheDTO {
        favoriteArtists = new LinkedHashSet<>(favoriteArtists);
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
        Set<ConfetiArtist> favoriteArtists,
        Set<String> exposedArtistIds
    ) {
        return new UserOnboardCacheDTO(favoriteArtists, exposedArtistIds);
    }

    public static UserOnboardCacheDTO empty() {
        return new UserOnboardCacheDTO(Collections.emptySet(), Collections.emptySet());
    }

    public UserOnboardCacheDTO withAddFavoriteArtistIds(
        Collection<ConfetiArtist> newFavoriteArtistIds
    ) {
        Set<ConfetiArtist> currentFavoriteArtistIds = new LinkedHashSet<>(this.favoriteArtists);
        currentFavoriteArtistIds.addAll(newFavoriteArtistIds);
        return new UserOnboardCacheDTO(currentFavoriteArtistIds, this.exposedArtistIds);
    }

    public UserOnboardCacheDTO deleteFavoriteArtistIds(Collection<String> targetArtistIds) {
        Set<String> targetIds = new HashSet<>(targetArtistIds);
        Set<ConfetiArtist> currentFavoriteArtists = new LinkedHashSet<>(this.favoriteArtists);

        currentFavoriteArtists.removeIf(targetIds::contains);

        return new UserOnboardCacheDTO(currentFavoriteArtists, this.exposedArtistIds);
    }
}
