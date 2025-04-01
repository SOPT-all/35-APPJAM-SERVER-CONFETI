package org.sopt.confeti.api.auth.dto.request;

import java.util.List;

public record OnboardRequest(
        List<OnboardArtistRequest> favoriteArtists
) {
}
