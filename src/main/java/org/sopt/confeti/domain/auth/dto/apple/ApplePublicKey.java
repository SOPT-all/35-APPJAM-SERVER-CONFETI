package org.sopt.confeti.domain.auth.dto.apple;

public record ApplePublicKey(
        String kty,
        String kid,
        String use,
        String alg,
        String n,
        String e
) {
}
