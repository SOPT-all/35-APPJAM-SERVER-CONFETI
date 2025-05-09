package org.sopt.confeti.auth;

import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.sopt.confeti.global.exception.ConfetiException;
import org.sopt.confeti.global.message.ErrorMessage;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OnboardService {
    public void validateFavoriteArtistCount(Set<String> favoriteArtistIds) {
        if (favoriteArtistIds.isEmpty()) {
            throw new ConfetiException(ErrorMessage.BAD_REQUEST);
        }
    }
}
