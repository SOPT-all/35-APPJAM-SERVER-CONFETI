package org.sopt.confeti.api.user.dto.response;

import org.sopt.confeti.domain.user.User;

public record UserInfoResponse (Long userId, String profileUrl, String username){
    public UserInfoResponse(User user) {
        this(user.getId(), user.getUsername(), user.getProfilePath());
    }
}
