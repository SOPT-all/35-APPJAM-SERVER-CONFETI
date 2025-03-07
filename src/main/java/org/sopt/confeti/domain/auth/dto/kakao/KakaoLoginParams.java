package org.sopt.confeti.domain.auth.dto.kakao;

import org.sopt.confeti.domain.auth.command.LoginCommand;

public record KakaoLoginParams(
        String redirectUrl,
        String code
){
    public static KakaoLoginParams from(LoginCommand command) {
        return new KakaoLoginParams(command.redirectUrl(), command.code());
    }
}
