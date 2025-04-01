package org.sopt.confeti.auth;

import lombok.RequiredArgsConstructor;
import org.sopt.confeti.api.auth.facade.dto.request.OnboardDTO;
import org.sopt.confeti.global.exception.ConfetiException;
import org.sopt.confeti.global.message.ErrorMessage;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OnboardService {
    private static final int MINIMUM_ARTIST_COUNT = 3;

    public void validateFavoriteArtistCount(OnboardDTO onboardDTO) {
        if (onboardDTO.favoriteArtists().size() < MINIMUM_ARTIST_COUNT) {
            throw new ConfetiException(ErrorMessage.BAD_REQUEST);
        }
    }
}
