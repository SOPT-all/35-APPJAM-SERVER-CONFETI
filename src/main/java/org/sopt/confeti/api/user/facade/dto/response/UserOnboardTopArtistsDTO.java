package org.sopt.confeti.api.user.facade.dto.response;

import java.util.List;
import org.sopt.confeti.global.resolver.music_api.artist.vo.ConfetiArtist;

public record UserOnboardTopArtistsDTO(
        List<UserOnboardTopArtistDTO> artists
) {
    public static UserOnboardTopArtistsDTO from(List<ConfetiArtist> artists) {
        return new UserOnboardTopArtistsDTO(
                artists.stream()
                        .map(UserOnboardTopArtistDTO::from)
                        .toList()
        );
    }
}
