package org.sopt.confeti.api.user.dto.response;

import org.sopt.confeti.api.user.facade.dto.response.UserFavoriteArtistListDTO;

import java.util.List;

public record UserFavoriteResponse(List<UserFavoriteListResponse> artists) {
    public static UserFavoriteResponse from(final List<UserFavoriteArtistListDTO> artistListDTO) {
        return new UserFavoriteResponse(
                artistListDTO.stream()
                        .map(UserFavoriteListResponse::from)
                        .toList()
        );
    }
}

