package org.sopt.confeti.domain.auth.command;

import org.sopt.confeti.domain.user.OAuthProvider;

public record LoginCommand(
        OAuthProvider provider,
        String redirectUrl,
        String code) {
}
