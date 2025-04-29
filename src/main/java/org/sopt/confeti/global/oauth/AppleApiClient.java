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
import java.util.HashMap;
import java.util.Map;
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
import org.sopt.confeti.global.module.rest_client.builder.ApiRestClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.MultiValueMap;

@OAuthClient
@RequiredArgsConstructor
public class AppleApiClient implements OAuthApiClient {

    @Value("${apple.client-id}")
    private String clientId;

    @Value("${apple.team-id}")
    private String teamId;

    @Value("${apple.key-id}")
    private String keyId;

    @Value("${apple.private-key}")
    private String privateKey;

    private static final String GRANT_TYPE = "authorization_code";
    private final String AAUTH_AUDIENCE_URL_HOST = "https://appleid.apple.com";
    private final String AAUTH_TOKEN_URL_HOST = "https://appleid.apple.com/auth/token";
    private final String AAUTH_PUBLIC_KEY_URL_HOST = "https://appleid.apple.com/auth/keys";
    private final int CLIENT_SECRET_EXPIRATION_MINUTE = 30;
    private final String PRIVATE_KEY_ALGORITHM = "EC";

    private final ApiRestClientBuilder restClient;

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

    @Override
    public void unlink(String socialId) {
        
    }

    private AppleTokenResult requestTokens(AppleTokenRequestParams requestParams) {
        Map<String, String> params = new HashMap<>();
        params.put("grant_type", requestParams.grantType());
        params.put("client_id", requestParams.clientId());
        params.put("client_secret", requestParams.clientSecret());
        params.put("code", requestParams.code());

        return restClient.request()
                .post()
                .baseUrl(AAUTH_TOKEN_URL_HOST)
                .params(MultiValueMap.fromSingleValue(params))
                .build()
                .connect()
                .retrieve(AppleTokenResult.class);
    }

    private ApplePublicKeys requestPublicKeys() {
        return restClient.request()
                .get()
                .baseUrl(AAUTH_PUBLIC_KEY_URL_HOST)
                .build()
                .connect()
                .retrieve(ApplePublicKeys.class);
    }

    private AppleSocialInfoResult getAppleSocialInfo(AppleTokenResult tokenResult, String name) {
        MyKeyLocator keyLocator = new MyKeyLocator(requestPublicKeys());

        Claims claims = Jwts.parser()
                .keyLocator(keyLocator)
                .build()
                .parseSignedClaims(tokenResult.idToken())
                .getPayload();

        return AppleSocialInfoResult.of(claims, name, tokenResult.accessToken(), tokenResult.refreshToken(),
                tokenResult.expiresIn());
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
