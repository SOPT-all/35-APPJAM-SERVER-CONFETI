package org.sopt.confeti.api.user.facade.dto.response;

import org.sopt.confeti.domain.user.User;

public record UserInfoDTO (
        long userId,
        String profilePath,
        String username
){
    public static UserInfoDTO from (final User user) {
        return new UserInfoDTO(
                user.getId(),
                user.getProfilePath(),
                user.getUsername()
        );
    }
}
