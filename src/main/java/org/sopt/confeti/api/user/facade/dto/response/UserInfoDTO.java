package org.sopt.confeti.api.user.facade.dto.response;

import org.sopt.confeti.domain.user.User;

public record UserInfoDTO (Long userId, String profileUrl, String username){
    public static UserInfoDTO from (User user) {
        return new UserInfoDTO(user.getId(), user.getProfilePath(), user.getUsername());
    }
}
