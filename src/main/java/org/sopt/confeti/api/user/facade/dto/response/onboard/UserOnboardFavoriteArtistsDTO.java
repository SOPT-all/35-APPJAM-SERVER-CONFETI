package org.sopt.confeti.api.user.facade.dto.response.onboard;

import java.util.List;
import org.sopt.confeti.global.resolver.music_api.artist.vo.ConfetiArtist;

public record UserOnboardFavoriteArtistsDTO(
    List<UserOnboardFavoriteArtistDTO> favoriteArtistIds
) {

    public static UserOnboardFavoriteArtistsDTO from(List<ConfetiArtist> confetiArtists) {
        return new UserOnboardFavoriteArtistsDTO(
            confetiArtists.stream()
                .map(UserOnboardFavoriteArtistDTO::from)
                .toList()
        );
    }
}
