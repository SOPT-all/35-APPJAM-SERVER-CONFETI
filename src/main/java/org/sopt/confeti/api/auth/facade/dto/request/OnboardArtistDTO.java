package org.sopt.confeti.api.auth.facade.dto.request;

public record OnboardArtistDTO(
        String artistId
) {
    public static OnboardArtistDTO from(String artistId) {
        return new OnboardArtistDTO(artistId);
    }
}
