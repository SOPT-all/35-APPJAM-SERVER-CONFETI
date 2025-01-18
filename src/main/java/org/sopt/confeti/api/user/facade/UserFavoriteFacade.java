package org.sopt.confeti.api.user.facade;

import lombok.RequiredArgsConstructor;
import org.sopt.confeti.annotation.Facade;
import org.sopt.confeti.api.user.facade.dto.response.UserFavoriteArtistDTO;
import org.sopt.confeti.domain.artistfavorite.ArtistFavorite;
import org.sopt.confeti.domain.artistfavorite.application.ArtistFavoriteService;
import org.sopt.confeti.domain.festival.Festival;
import org.sopt.confeti.domain.festival.application.FestivalService;
import org.sopt.confeti.domain.festivalfavorite.application.FestivalFavoriteService;
import org.sopt.confeti.domain.user.User;
import org.sopt.confeti.domain.user.application.UserService;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Facade
@RequiredArgsConstructor
public class UserFavoriteFacade {

    private final UserService userService;
    private final FestivalService festivalService;
    private final FestivalFavoriteService festivalFavoriteService;
    private final ArtistFavoriteService artistFavoriteService;

    @Transactional
    public void addFestivalFavorite(long userId, long festivalId) {
        User user = userService.findById(userId);
        Festival festival = festivalService.findById(festivalId);
        festivalFavoriteService.save(user, festival);
    }

    @Transactional
    public void removeFestivalFavorite(long userId, long festivalId) {
        User user = userService.findById(userId);
        Festival festival = festivalService.findById(festivalId);
        festivalFavoriteService.delete(user, festival);
    }

    @Transactional(readOnly = true)
    public UserFavoriteArtistDTO getArtistList(long userId) {
        userService.existsById(userId);
        List<ArtistFavorite> artists = artistFavoriteService.getArtistList(userId);
        return UserFavoriteArtistDTO.from(artists);
    }
}
