package org.sopt.confeti.auth.jwt;

import io.jsonwebtoken.io.Decoders;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import org.springframework.stereotype.Component;

@Component
public class KeyGenerator {
    private static final String HMACSHA256 = "HmacSHA256";

    public SecretKey getKeyFromString(String keyString) {
        byte[] keyBytes = Decoders.BASE64.decode(keyString);
        return new SecretKeySpec(keyBytes, HMACSHA256);
    }
}
