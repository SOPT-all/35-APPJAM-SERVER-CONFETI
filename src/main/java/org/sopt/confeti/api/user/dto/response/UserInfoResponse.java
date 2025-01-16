package org.sopt.confeti.api.user.dto.response;

import org.sopt.confeti.api.user.facade.dto.response.UserInfoDTO;

public record UserInfoResponse (Long userId, String profileUrl, String username){
    public static UserInfoResponse from (UserInfoDTO userInfoDTO) {
        return new UserInfoResponse(userInfoDTO.userId(), userInfoDTO.profileUrl(), userInfoDTO.username());
    }
}