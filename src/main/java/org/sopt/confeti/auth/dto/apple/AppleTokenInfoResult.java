package org.sopt.confeti.auth.dto.apple;

import org.sopt.confeti.global.exception.ConfetiException;
import org.sopt.confeti.global.message.ErrorMessage;

public record AppleTokenInfoResult(
        String accessToken,
        String refreshToken,
        long expiresIn
) {
    public static AppleTokenInfoResult create(String accessToken, String refreshToken, String expiresIn) {
        try {
            return new AppleTokenInfoResult(
                    accessToken, refreshToken, Long.parseLong(expiresIn)
            );
        } catch (NumberFormatException e) {
            throw new ConfetiException(ErrorMessage.INTERNAL_SERVER_ERROR);
        }
    }
}
