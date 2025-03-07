package org.sopt.confeti.domain.auth.dto;

public record OAuthSocialInfoResult(
        String id,
        String name,
        String profileImgUrl
) {
    public static OAuthSocialInfoResult from(KakaoSocialInfoResult kakaoInfo) {
        return new OAuthSocialInfoResult(
                kakaoInfo.id(),
                kakaoInfo.kakaoAccount().profile().nickname(),
                kakaoInfo.kakaoAccount().profile().profileImageUrl()
        );
    }
}
