package org.sopt.confeti.domain.auth.dto.apple;

import java.util.List;

public record ApplePublicKeys(
        List<ApplePublicKey> keys
) {
}
