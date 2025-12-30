package org.sopt.confeti.global.interceptor.auth;

import lombok.Builder;
import org.sopt.confeti.domain.user.OAuthProvider;
import org.sopt.confeti.domain.user.constant.Role;

@Builder
public record UserInfo(
    long id,
    OAuthProvider provider,
    String socialId,
    String name,
    String profilePath,
    Role role,
    boolean hasTimetableHistory
) {

}
