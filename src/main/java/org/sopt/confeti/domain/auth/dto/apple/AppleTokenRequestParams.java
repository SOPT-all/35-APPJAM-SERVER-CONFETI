package org.sopt.confeti.domain.auth.dto.apple;

public record AppleTokenRequestParams(
        String clientId,
        String clientSecret,
        String grantType,
        String code
) {
    public static AppleTokenRequestParams of(String clientId, String clientSecret, String grantType, String code) {
        return new AppleTokenRequestParams(clientId, clientSecret, grantType, code);
    }
}
