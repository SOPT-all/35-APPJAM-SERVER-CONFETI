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
    public void save(long userId, long festivalId) {
        User user = userService.findById(userId);
        Festival festival = festivalService.findById(festivalId);
        festivalFavoriteService.save(user, festival);
    }

    @Transactional
    public void delete(long userId, long festivalId) {
        User user = userService.findById(userId);
        Festival festival = festivalService.findById(festivalId);
        festivalFavoriteService.delete(user, festival);
    }

    @Transactional
    public UserFavoriteArtistDTO getArtistList(long userId) {
        User user = userService.findById(userId);
        List<ArtistFavorite> artists = artistFavoriteService.getArtistList(user);
        return UserFavoriteArtistDTO.of(artists);
    }
}
