package org.sopt.confeti.api.user.facade.dto.response.onboard;

import java.util.List;
import org.sopt.confeti.global.resolver.music_api.artist.vo.ConfetiArtist;

public record UserOnboardRelatedArtistsDTO(
        List<UserOnboardRelatedArtistDTO> artists
) {
    public static UserOnboardRelatedArtistsDTO from(List<ConfetiArtist> artists) {
        return new UserOnboardRelatedArtistsDTO(
                artists.stream()
                        .map(UserOnboardRelatedArtistDTO::from)
                        .toList()
        );
    }
}
