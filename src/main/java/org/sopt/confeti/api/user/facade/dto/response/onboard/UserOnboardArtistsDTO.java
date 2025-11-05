package org.sopt.confeti.api.user.facade.dto.response.onboard;

import java.util.List;
import org.sopt.confeti.global.resolver.music_api.artist.vo.ConfetiArtist;

public record UserOnboardArtistsDTO(
    List<UserOnboardArtistDTO> artists
) {

    public static UserOnboardArtistsDTO from(List<ConfetiArtist> artists) {
        return new UserOnboardArtistsDTO(
            artists.stream()
                .map(UserOnboardArtistDTO::from)
                .toList()
        );
    }
}
