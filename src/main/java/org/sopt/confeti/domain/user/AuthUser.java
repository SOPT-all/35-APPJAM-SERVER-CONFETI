package org.sopt.confeti.domain.user;

import lombok.Getter;

@Getter
public class AuthUser {

    private long id;
    private final OAuthProvider provider;
    private final String socialNickname;
    private final String socialProfile;

    private AuthUser(OAuthProvider provider, String socialNickname, String socialProfile) {
        this.provider = provider;
        this.socialNickname = socialNickname;
        this.socialProfile = socialProfile;
    }

    private AuthUser(long id, OAuthProvider provider, String socialNickname, String socialProfile) {
        this.id=id;
        this.provider=provider;
        this.socialNickname = socialNickname;
        this.socialProfile=socialProfile;
    }

    public static AuthUser create(OAuthProvider provider, String socialNickname, String socialProfile) {
        return new AuthUser(provider, socialNickname, socialProfile);
    }

    public static AuthUser createWithId(long id, OAuthProvider provider, String socialNickname, String socialProfile) {
        return new AuthUser(id, provider, socialNickname, socialProfile);
    }

    public static AuthUser toAuthUser(User user) {
        return new AuthUser(
                user.getId(),
                user.getOauthProvider(),
                user.getUsername(),
                user.getProfilePath()
        );
    }
}
