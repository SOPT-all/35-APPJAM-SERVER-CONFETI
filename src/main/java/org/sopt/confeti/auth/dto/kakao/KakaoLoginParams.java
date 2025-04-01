package org.sopt.confeti.auth.dto.kakao;

import org.sopt.confeti.auth.command.LoginCommand;

public record KakaoLoginParams(
        String redirectUrl,
        String code
) {
    public static KakaoLoginParams from(LoginCommand command) {
        return new KakaoLoginParams(command.redirectUrl(), command.code());
    }
}
