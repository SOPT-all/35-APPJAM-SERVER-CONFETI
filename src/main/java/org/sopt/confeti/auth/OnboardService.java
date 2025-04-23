package org.sopt.confeti.auth;

import lombok.RequiredArgsConstructor;
import org.sopt.confeti.api.auth.facade.dto.request.OnboardDTO;
import org.sopt.confeti.global.exception.ConfetiException;
import org.sopt.confeti.global.message.ErrorMessage;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OnboardService {
    public void validateFavoriteArtistCount(OnboardDTO onboardDTO) {
        if (onboardDTO.favoriteArtists().isEmpty()) {
            throw new ConfetiException(ErrorMessage.BAD_REQUEST);
        }
    }
}
