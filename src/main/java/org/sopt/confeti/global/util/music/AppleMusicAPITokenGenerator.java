package org.sopt.confeti.global.util.music;

import io.jsonwebtoken.Jwts;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.sopt.confeti.global.annotation.Generator;
import org.sopt.confeti.global.exception.ConfetiException;
import org.sopt.confeti.global.message.ErrorMessage;
import org.springframework.beans.factory.annotation.Value;

@Slf4j
@Generator
@RequiredArgsConstructor
public class AppleMusicAPITokenGenerator {

    private final String PRIVATE_KEY_ALGORITHM = "EC";

    @Value("${apple-music.credentials.key-id}")
    private String keyId;

    @Value("${apple-music.credentials.team-id}")
    private String teamId;

    @Value("${apple-music.credentials.private-key}")
    private String privateKey;

    @Value("${apple-music.credentials.expiration}")
    private Long expiration;

    public String generateToken() {
        Date now = new Date();

        Map<String, Object> claims = new HashMap<>();
        claims.put("iss", teamId);
        claims.put("iat", now);
        claims.put("exp", new Date(now.getTime() + expiration));

        return Jwts.builder()
                .header().keyId(keyId).add("alg", "ES256").and()
                .claims(claims)
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
            log.debug(e.getMessage());
            throw new ConfetiException(ErrorMessage.INTERNAL_SERVER_ERROR);
        }
    }
}
