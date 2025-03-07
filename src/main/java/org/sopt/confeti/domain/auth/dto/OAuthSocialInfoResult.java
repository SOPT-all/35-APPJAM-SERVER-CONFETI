package org.sopt.confeti.domain.auth.dto;

import org.sopt.confeti.domain.auth.dto.apple.AppleSocialInfoResult;
import org.sopt.confeti.domain.auth.dto.kakao.KakaoSocialInfoResult;

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

    public static OAuthSocialInfoResult from(AppleSocialInfoResult appleInfo) {
        return new OAuthSocialInfoResult(
                appleInfo.id(),
                appleInfo.name(),
                null
        );
    }
}
