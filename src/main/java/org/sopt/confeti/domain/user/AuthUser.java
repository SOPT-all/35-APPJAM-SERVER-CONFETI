package org.sopt.confeti.domain.user;

import lombok.Getter;

@Getter
public class AuthUser {

    private long id;
    private final OAuthProvider provider;
    private final String socialId;
    private final String socialNickname;
    private final String socialProfile;

    private AuthUser(OAuthProvider provider,String socialId, String socialNickname, String socialProfile) {
        this.provider = provider;
        this.socialId = socialId;
        this.socialNickname = socialNickname;
        this.socialProfile = socialProfile;
    }

    private AuthUser(long id, OAuthProvider provider, String socialId, String socialNickname, String socialProfile) {
        this.id=id;
        this.provider=provider;
        this.socialId = socialId;
        this.socialNickname = socialNickname;
        this.socialProfile=socialProfile;
    }

    public static AuthUser create(OAuthProvider provider,String socialId, String socialNickname, String socialProfile) {
        return new AuthUser(provider, socialId, socialNickname, socialProfile);
    }

    public static AuthUser createWithId(long id, OAuthProvider provider, String socialId, String socialNickname, String socialProfile) {
        return new AuthUser(id, provider, socialId, socialNickname, socialProfile);
    }

    public static AuthUser toAuthUser(User user) {
        return new AuthUser(
                user.getId(),
                user.getProvider(),
                user.getSocialId(),
                user.getUsername(),
                user.getProfilePath()
        );
    }
}
