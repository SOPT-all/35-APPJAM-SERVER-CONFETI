package org.sopt.confeti.domain.auth.jwt;

import io.jsonwebtoken.JwsHeader;
import io.jsonwebtoken.LocatorAdapter;
import java.math.BigInteger;
import java.security.Key;
import java.security.KeyFactory;
import java.security.spec.KeySpec;
import java.security.spec.RSAPublicKeySpec;
import java.util.Base64;
import lombok.RequiredArgsConstructor;
import org.sopt.confeti.domain.auth.dto.apple.ApplePublicKey;
import org.sopt.confeti.domain.auth.dto.apple.ApplePublicKeys;
import org.sopt.confeti.global.exception.ConfetiException;
import org.sopt.confeti.global.message.ErrorMessage;

@RequiredArgsConstructor
public class MyKeyLocator extends LocatorAdapter<Key> {

    private final ApplePublicKeys publicKeys;

    @Override
    protected Key locate(JwsHeader header) {
        ApplePublicKey publicKey = publicKeys.keys().stream()
                .filter(applePublicKey -> applePublicKey.kid().equals(header.getKeyId()))
                .findFirst()
                .orElseThrow(
                        () -> new ConfetiException(ErrorMessage.INTERNAL_SERVER_ERROR)
                );

        BigInteger n = new BigInteger(1, Base64.getUrlDecoder().decode(publicKey.n()));
        BigInteger e = new BigInteger(1, Base64.getUrlDecoder().decode(publicKey.e()));

        try {
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            KeySpec keySpec = new RSAPublicKeySpec(n, e);

            return keyFactory.generatePublic(keySpec);
        } catch (Exception er) {
            throw new ConfetiException(ErrorMessage.INTERNAL_SERVER_ERROR);
        }
    }
}
