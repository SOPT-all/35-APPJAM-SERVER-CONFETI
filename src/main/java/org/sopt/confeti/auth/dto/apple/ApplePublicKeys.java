package org.sopt.confeti.auth.dto.apple;

import java.util.List;

public record ApplePublicKeys(
        List<ApplePublicKey> keys
) {
}
