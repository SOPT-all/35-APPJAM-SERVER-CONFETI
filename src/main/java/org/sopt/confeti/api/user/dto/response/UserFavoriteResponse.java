package org.sopt.confeti.api.user.dto.response;

import java.util.List;
import org.sopt.confeti.api.user.facade.dto.response.UserFavoriteArtistListDTO;

public record UserFavoriteResponse(
        List<UserFavoriteListResponse> artists
) {
    public static UserFavoriteResponse from(final List<UserFavoriteArtistListDTO> artistListDTO) {
        return new UserFavoriteResponse(
                artistListDTO.stream()
                        .map(UserFavoriteListResponse::from)
                        .toList()
        );
    }
}

