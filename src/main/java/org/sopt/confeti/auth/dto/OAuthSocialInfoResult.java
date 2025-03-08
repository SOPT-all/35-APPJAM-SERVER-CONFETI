package org.sopt.confeti.auth.dto;

import org.sopt.confeti.auth.dto.apple.AppleSocialInfoResult;
import org.sopt.confeti.auth.dto.kakao.KakaoSocialInfoResult;

public record OAuthSocialInfoResult(
        String id,
        String name,
        String profileImgUrl,
        OAuthTokenInfoResult token
) {
    public static OAuthSocialInfoResult from(KakaoSocialInfoResult kakaoInfo) {
        return new OAuthSocialInfoResult(
                kakaoInfo.id(),
                kakaoInfo.kakaoAccount().profile().nickname(),
                kakaoInfo.kakaoAccount().profile().profileImageUrl(),
                null
        );
    }

    public static OAuthSocialInfoResult from(AppleSocialInfoResult appleInfo) {
        return new OAuthSocialInfoResult(
                appleInfo.id(),
                appleInfo.name(),
                null,
                OAuthTokenInfoResult.from(appleInfo.token())
        );
    }
}
