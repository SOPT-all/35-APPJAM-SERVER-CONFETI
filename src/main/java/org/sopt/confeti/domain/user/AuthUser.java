package org.sopt.confeti.domain.user;

import lombok.Getter;
import org.sopt.confeti.domain.user.constant.Role;

@Getter
public class AuthUser {

    private Long id;
    private final OAuthProvider provider;
    private final String socialId;
    private final String socialNickname;
    private final String socialProfile;
    private final Role role;

    private AuthUser(OAuthProvider provider, String socialId, String socialNickname, String socialProfile) {
        this.provider = provider;
        this.socialId = socialId;
        this.socialNickname = socialNickname;
        this.socialProfile = socialProfile;
        this.role = Role.ONBOARDING;
    }

    private AuthUser(Long id, OAuthProvider provider, String socialId, String socialNickname, String socialProfile,
                     Role role) {
        this.id = id;
        this.provider = provider;
        this.socialId = socialId;
        this.socialNickname = socialNickname;
        this.socialProfile = socialProfile;
        this.role = role;
    }

    public static AuthUser create(OAuthProvider provider, String socialId, String socialNickname,
                                  String socialProfile) {
        return new AuthUser(provider, socialId, socialNickname, socialProfile);
    }

    public static AuthUser createWithId(Long id, OAuthProvider provider, String socialId, String socialNickname,
                                        String socialProfile, Role role) {
        return new AuthUser(id, provider, socialId, socialNickname, socialProfile, role);
    }

    public static AuthUser toAuthUser(User user) {
        return new AuthUser(
                user.getId(),
                user.getProvider(),
                user.getSocialId(),
                user.getName(),
                user.getProfilePath(),
                user.getRole()
        );
    }
}
