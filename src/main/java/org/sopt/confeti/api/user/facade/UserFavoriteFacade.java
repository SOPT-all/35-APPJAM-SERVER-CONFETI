package org.sopt.confeti.api.user.facade;

import java.time.LocalDate;
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
        validateNotExistFestivalFavorite(userId, festivalId);

        festivalFavoriteService.save(user, festival);
    }

    @Transactional
    public void removeFestivalFavorite(long userId, long festivalId) {
        // TODO: 페스티벌 좋아요 삭제 시 엔티티 값을 사용하지 않으므로 아이디 값으로 삭제하도록 리펙토링 예정
        User user = userService.findById(userId);
        Festival festival = festivalService.findById(festivalId);
        validateExistFestivalFavorite(userId, festivalId);

        festivalFavoriteService.delete(user, festival);
    }

    @Transactional(readOnly = true)
    protected void validateExistFestival(final long festivalId) {
        if (!festivalService.existsById(festivalId)) {
            throw new NotFoundException(ErrorMessage.NOT_FOUND);
        }
    }

    @Transactional(readOnly = true)
    protected void validateExistFestivalFavorite(final long userId, final long festivalId) {
        if (!festivalFavoriteService.isFavorite(userId, festivalId)) {
            throw new NotFoundException(ErrorMessage.NOT_FOUND);
        }
    }

    @Transactional(readOnly = true)
    protected void validateNotExistFestivalFavorite(final long userId, final long festivalId) {
        if (festivalFavoriteService.isFavorite(userId, festivalId)) {
            throw new ConflictException(ErrorMessage.CONFLICT);
        }
    }

    @Transactional(readOnly = true)
    public UserFavoriteArtistDTO getArtistList(long userId) {
        validateExistUser(userId);

        List<ArtistFavorite> artists = artistFavoriteService.getArtistList(userId);
        return UserFavoriteArtistDTO.from(artists);
    }

    @Transactional
    public void addArtistFavorite(final long userId, final String artistId) {
        User user = userService.findById(userId);
        validateNotExistArtistFavorite(userId, artistId);

        artistFavoriteService.addFavorite(user, artistId);
    }

    @Transactional
    public void removeArtistFavorite(final long userId, final String artistId) {
        validateExistUser(userId);
        validateExistArtistFavorite(userId, artistId);

        artistFavoriteService.removeFavorite(userId, artistId);
    }

    @Transactional(readOnly = true)
    protected void validateExistArtistFavorite(final long userId, final String artistId) {
        if (!artistFavoriteService.isFavorite(userId, artistId)) {
            throw new NotFoundException(ErrorMessage.NOT_FOUND);
        }
    }

    @Transactional(readOnly = true)
    protected void validateNotExistArtistFavorite(final long userId, final String artistId) {
        if (artistFavoriteService.isFavorite(userId, artistId)) {
            throw new ConflictException(ErrorMessage.CONFLICT);
        }
    }

    @Transactional
    public void addConcertFavorite(final long userId, final long concertId) {
        User user = userService.findById(userId);
        Concert concert = concertService.findById(concertId);

        validateExistConcertFavorite(userId, concertId);

        concertFavoriteService.addFavorite(user, concert);
    }

    @Transactional
    public void removeConcertFavorite(final long userId, final long concertId) {
        validateExistUser(userId);
        validateExistConcert(concertId);
        validateExistConcertFavorite(userId, concertId);

        concertFavoriteService.removeFavorite(userId, concertId);
    }

    @Transactional(readOnly = true)
    protected void validateExistConcertFavorite(final long userId, final long concertId) {
        if (!concertFavoriteService.isFavorite(userId, concertId)) {
            throw new NotFoundException(ErrorMessage.NOT_FOUND);
        }
    }

    @Transactional(readOnly = true)
    protected void validateExistUser(final long userId) {
        if (!userService.existsById(userId)) {
            throw new NotFoundException(ErrorMessage.NOT_FOUND);
        }
    }

    @Transactional(readOnly = true)
    protected void validateExistConcert(final long concertId) {
        if (!concertService.existsById(concertId)) {
            throw new NotFoundException(ErrorMessage.NOT_FOUND);
        }
    }
}
