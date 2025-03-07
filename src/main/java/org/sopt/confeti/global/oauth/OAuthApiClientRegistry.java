package org.sopt.confeti.global.oauth;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.sopt.confeti.domain.user.OAuthProvider;
import org.sopt.confeti.global.annotation.Registry;
import org.sopt.confeti.global.exception.ConfetiException;
import org.sopt.confeti.global.message.ErrorMessage;

@Registry
@RequiredArgsConstructor
public class OAuthApiClientRegistry {

    private List<OAuthApiClient> oAuthApiClients;

    public OAuthApiClient getOAuthApiClientByProvider(OAuthProvider provider) {
        return oAuthApiClients.stream()
                .filter(oAuthApiClient -> oAuthApiClient.supports(provider))
                .findFirst()
                .orElseThrow(
                        () -> new ConfetiException(ErrorMessage.BAD_REQUEST)
                );
    }
}
