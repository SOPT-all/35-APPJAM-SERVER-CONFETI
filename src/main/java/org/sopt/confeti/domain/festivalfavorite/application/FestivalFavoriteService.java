package org.sopt.confeti.domain.festivalfavorite.application;

import org.sopt.confeti.domain.festival.Festival;
import org.sopt.confeti.domain.festivalfavorite.FestivalFavorite;
import org.sopt.confeti.domain.festivalfavorite.infra.repository.FestivalFavoriteRepository;
import org.sopt.confeti.domain.user.User;
import org.sopt.confeti.global.exception.ConflictException;
import org.sopt.confeti.global.exception.NotFoundException;
import org.sopt.confeti.global.message.ErrorMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FestivalFavoriteService {
    private final FestivalFavoriteRepository festivalFavoriteRepository;

    @Autowired
    public FestivalFavoriteService(
            FestivalFavoriteRepository festivalFavoriteRepository) {
        this.festivalFavoriteRepository = festivalFavoriteRepository;
    }

    public void save(User user, Festival festival) {
        festivalFavoriteRepository.findByUserIdAndFestivalId(user.getId(), festival.getId())
                .ifPresent(festivalFavorite -> {
                    throw new ConflictException(ErrorMessage.CONFLICT);
                });

        FestivalFavorite festivalFavorite = FestivalFavorite.create(user, festival);
        festivalFavoriteRepository.save(festivalFavorite);
    }

    public void delete(User user, Festival festival) {
        FestivalFavorite festivalFavorite = festivalFavoriteRepository.findByUserIdAndFestivalId(user.getId(), festival.getId())
                .orElseThrow(() -> new NotFoundException(ErrorMessage.NOT_FOUND));

        festivalFavoriteRepository.delete(festivalFavorite);
    }
}