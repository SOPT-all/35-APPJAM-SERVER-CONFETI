package org.sopt.confeti.api.user.dto.response;

import java.util.List;
import org.sopt.confeti.api.user.facade.dto.response.UserFavoriteArtistPreviewDTO;

public record UserFavoriteArtistsPreviewResponse(
        List<UserFavoriteArtistPreviewResponse> artists
) {
    public static UserFavoriteArtistsPreviewResponse from(final List<UserFavoriteArtistPreviewDTO> artistListDTO) {
        return new UserFavoriteArtistsPreviewResponse(
                artistListDTO.stream()
                        .map(UserFavoriteArtistPreviewResponse::from)
                        .toList()
        );
    }
}

