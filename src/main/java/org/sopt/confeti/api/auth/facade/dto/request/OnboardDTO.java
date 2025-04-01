package org.sopt.confeti.api.auth.facade.dto.request;

import java.util.List;
import java.util.Set;
import org.sopt.confeti.api.auth.dto.request.OnboardArtistRequest;
import org.sopt.confeti.api.auth.dto.request.OnboardRequest;

public record OnboardDTO(
        List<OnboardArtistDTO> favoriteArtists
) {

    public static OnboardDTO from(OnboardRequest request) {
        Set<String> artistIds = Set.copyOf(
                request.favoriteArtists().stream()
                        .map(OnboardArtistRequest::artistId)
                        .toList()
        );

        return new OnboardDTO(
                artistIds.stream()
                        .map(OnboardArtistDTO::from)
                        .toList()
        );
    }
}
