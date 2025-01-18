package org.sopt.confeti.api.user.facade;

import lombok.RequiredArgsConstructor;
import org.sopt.confeti.annotation.Facade;
import org.sopt.confeti.api.user.facade.dto.response.UserFavoriteArtistDTO;
import org.sopt.confeti.domain.artistfavorite.ArtistFavorite;
import org.sopt.confeti.domain.artistfavorite.application.ArtistFavoriteService;
import org.sopt.confeti.domain.concert.application.ConcertService;
import org.sopt.confeti.domain.concertfavorite.application.ConcertFavoriteService;
import org.sopt.confeti.domain.concert.Concert;
import org.sopt.confeti.domain.concert.application.ConcertService;
import org.sopt.confeti.domain.concertfavorite.application.ConcertFavoriteService;
import org.sopt.confeti.domain.festival.Festival;
import org.sopt.confeti.domain.festival.application.FestivalService;
import org.sopt.confeti.domain.festivalfavorite.application.FestivalFavoriteService;
import org.sopt.confeti.domain.user.User;
import org.sopt.confeti.domain.user.application.UserService;
import org.sopt.confeti.global.exception.ConflictException;
import org.sopt.confeti.global.exception.NotFoundException;
import org.sopt.confeti.global.message.ErrorMessage;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Facade
@RequiredArgsConstructor
public class UserFavoriteFacade {

    private final UserService userService;
    private final FestivalService festivalService;
    private final FestivalFavoriteService festivalFavoriteService;
    private final ArtistFavoriteService artistFavoriteService;
    private final ConcertFavoriteService concertFavoriteService;
    private final ConcertService concertService;

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

    @Transactional
    public void addArtistFavorite(final long userId, final String artistId) {
        User user = userService.findById(userId);

        if (artistFavoriteService.isFavorite(userId, artistId)) {
            throw new ConflictException(ErrorMessage.CONFLICT);
        }

        artistFavoriteService.addFavorite(user, artistId);
    }

    @Transactional
    public void removeArtistFavorite(final long userId, final String artistId) {
        userService.existsById(userId);

        if (!artistFavoriteService.isFavorite(userId, artistId)) {
            throw new NotFoundException(ErrorMessage.NOT_FOUND);
        }

        artistFavoriteService.removeFavorite(userId, artistId);
    }

    @Transactional
    public void addConcertFavorite(final long userId, final long concertId) {
        User user = userService.findById(userId);
        Concert concert = concertService.findById(concertId);

        if (concertFavoriteService.isFavorite(userId, concertId)) {
            throw new ConflictException(ErrorMessage.CONFLICT);
        }

        concertFavoriteService.addFavorite(user, concert);
    }

    @Transactional
    public void removeConcertFavorite(final long userId, final long concertId) {
        userService.existsById(userId);
        concertService.existsById(concertId);

        if (!concertFavoriteService.isFavorite(userId, concertId)) {
            throw new NotFoundException(ErrorMessage.NOT_FOUND);
        }

        concertFavoriteService.removeFavorite(userId, concertId);
    }
}
