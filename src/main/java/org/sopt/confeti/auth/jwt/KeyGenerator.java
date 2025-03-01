package org.sopt.confeti.auth.jwt;

import io.jsonwebtoken.io.Decoders;
import org.springframework.stereotype.Component;

import javax.crypto.spec.SecretKeySpec;
import java.security.Key;

@Component
public class KeyGenerator {
    private static final String HMACSHA256 = "HmacSHA256";
    public Key getKeyFromString(String keyString) {
        byte[] keyBytes = Decoders.BASE64.decode(keyString);
        return new SecretKeySpec(keyBytes, HMACSHA256);
    }
}
