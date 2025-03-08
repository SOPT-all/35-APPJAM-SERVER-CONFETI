package org.sopt.confeti.global.oauth;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Base64;
import java.util.Date;
import lombok.RequiredArgsConstructor;
import org.sopt.confeti.auth.command.LoginCommand;
import org.sopt.confeti.auth.dto.OAuthSocialInfoResult;
import org.sopt.confeti.auth.dto.apple.ApplePublicKeys;
import org.sopt.confeti.auth.dto.apple.AppleSocialInfoResult;
import org.sopt.confeti.auth.dto.apple.AppleTokenRequestParams;
import org.sopt.confeti.auth.dto.apple.AppleTokenResult;
import org.sopt.confeti.auth.jwt.MyKeyLocator;
import org.sopt.confeti.domain.user.OAuthProvider;
import org.sopt.confeti.global.annotation.OAuthClient;
import org.sopt.confeti.global.exception.ConfetiException;
import org.sopt.confeti.global.message.ErrorMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestClient;

@OAuthClient
@RequiredArgsConstructor
public class AppleApiClient implements OAuthApiClient {

    @Value("${apple.client-id}")
    private String clientId;

    @Value("${apple.team-id}")
    private String teamId;

    @Value("${apple.key-id}")
    private String keyId;

    @Value("${apple.private-key")
    private String privateKey;

    private static final String GRANT_TYPE = "authorization_code";
    private final String AAUTH_AUDIENCE_URL_HOST = "https://appleid.apple.com";
    private final String AAUTH_TOKEN_URL_HOST = "https://appleid.apple.com/auth/token";
    private final String AAUTH_PUBLIC_KEY_URL_HOST = "https://appleid.apple.com/auth/keys";
    private final int CLIENT_SECRET_EXPIRATION_MINUTE = 30;
    private final String PRIVATE_KEY_ALGORITHM = "EC";

    private final RestClient restClient;

    @Override
    public boolean supports(OAuthProvider provider) {
        return provider == OAuthProvider.APPLE;
    }

    @Override
    public OAuthSocialInfoResult getSocialInfo(LoginCommand command) {
        AppleTokenResult tokenResult = requestTokens(
                AppleTokenRequestParams.of(clientId, generateClientSecret(), GRANT_TYPE, command.code())
        );
        AppleSocialInfoResult socialInfo = getAppleSocialInfo(tokenResult, command.name());
        return OAuthSocialInfoResult.from(socialInfo);
    }

    private AppleTokenResult requestTokens(AppleTokenRequestParams params) {
        return restClient
                .method(HttpMethod.POST)
                .uri(AAUTH_TOKEN_URL_HOST)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(createHttpBody(params))
                .retrieve()
                .toEntity(AppleTokenResult.class)
                .getBody();
    }

    private ApplePublicKeys requestPublicKeys() {
        return restClient
                .method(HttpMethod.GET)
                .uri(AAUTH_PUBLIC_KEY_URL_HOST)
                .retrieve()
                .toEntity(ApplePublicKeys.class)
                .getBody();
    }

    private AppleSocialInfoResult getAppleSocialInfo(AppleTokenResult tokenResult, String name) {
        MyKeyLocator keyLocator = new MyKeyLocator(requestPublicKeys());

        Claims claims = Jwts.parser()
                .keyLocator(keyLocator)
                .build()
                .parseSignedClaims(tokenResult.idToken())
                .getPayload();

        return AppleSocialInfoResult.of(claims, name, tokenResult.accessToken(), tokenResult.refreshToken(), tokenResult.expiresIn());
    }

    private String createHttpBody(AppleTokenRequestParams params) {
        return "grant_type=" + params.grantType() +
                "&client_id=" + params.clientId() +
                "&client_secret=" + params.clientSecret() +
                "&code=" + params.code();
    }

    private String generateClientSecret() {
        LocalDateTime expiration = LocalDateTime.now().plusMinutes(CLIENT_SECRET_EXPIRATION_MINUTE);

        return Jwts.builder()
                .header().keyId(keyId).and()
                .issuer(teamId)
                .audience().add(AAUTH_AUDIENCE_URL_HOST).and()
                .subject(clientId)
                .expiration(Date.from(expiration.atZone(ZoneId.systemDefault()).toInstant()))
                .issuedAt(new Date())
                .signWith(getPrivateKey())
                .compact();
    }

    private PrivateKey getPrivateKey() {
        try {
            byte[] privateKeyBytes = Base64.getDecoder().decode(privateKey);
            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(privateKeyBytes);
            KeyFactory keyFactory = KeyFactory.getInstance(PRIVATE_KEY_ALGORITHM);
            return keyFactory.generatePrivate(keySpec);
        } catch (Exception e) {
            throw new ConfetiException(ErrorMessage.INTERNAL_SERVER_ERROR);
        }
    }
}
