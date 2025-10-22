package org.sopt.confeti.api.auth.dto.request;

import java.util.List;

@Deprecated
public record OnboardRequest(
    List<OnboardArtistRequest> favoriteArtists
) {

}
