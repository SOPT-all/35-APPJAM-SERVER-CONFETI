package org.sopt.confeti.domain.festival.application;

import jakarta.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.sopt.confeti.api.performance.facade.dto.request.CreateFestivalDTO;
import org.sopt.confeti.domain.festival.Festival;
import org.sopt.confeti.domain.festival.application.dto.FestivalCursorDTO;
import org.sopt.confeti.domain.festival.infra.repository.FestivalRepository;
import org.sopt.confeti.global.exception.NotFoundException;
import org.sopt.confeti.global.message.ErrorMessage;
import org.sopt.confeti.global.util.artistsearcher.ArtistResolver;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class FestivalService {

    private static final String START_AT_COLUMN = "festivalStartAt";

    private final FestivalRepository festivalRepository;
    private final ArtistResolver artistResolver;

    private static final int INIT_PAGE = 0;
    private static final String FESTIVAL_TITLE_COLUMN_NAME = "festivalTitle";

    @Transactional(readOnly = true)
    public Festival findById(Long festivalId) {
        Festival festival = festivalRepository.findById(festivalId)
                .orElseThrow(() -> new NotFoundException(ErrorMessage.NOT_FOUND));
        return festival;
    }

    @Transactional(readOnly = true)
    public Festival getFestivalDetailByFestivalId(final long festivalId) {
        Festival festival = festivalRepository.findById(festivalId)
                .orElseThrow(() -> new NotFoundException(ErrorMessage.NOT_FOUND));
        artistResolver.load(festival);

        return festival;
    }

    @Transactional
    public Festival create(final CreateFestivalDTO createFestivalDTO) {

        return festivalRepository.save(
                Festival.create(createFestivalDTO)
        );
    }

    @Transactional(readOnly = true)
    public boolean existsById(final long festivalId) {
        return festivalRepository.existsById(festivalId);
    }

    @Transactional
    public List<Festival> findFestivalsByIdIn(final List<Long> festivalIds) {
        return festivalRepository.findFestivalsByIdIn(festivalIds);
    }


    @Transactional(readOnly = true)
    public List<Festival> findFestivalsUsingInitCursor(final long userId, final int size) {
        return festivalRepository.findFestivalsUsingInitCursor(
                userId,
                getPageRequestWithSort(size, getFestivalSort())
        );
    }

    private PageRequest getPageRequestWithSort(final int size, final Sort sort) {
        return PageRequest.of(INIT_PAGE, size, sort);
    }

    private Sort getFestivalSort() {
        return Sort.by(
                Order.asc(FESTIVAL_TITLE_COLUMN_NAME)
        );
    }

    @Transactional(readOnly = true)
    public List<Festival> findFestivalsUsingCursor(
            final long userId,
            final String cursorTitle,
            final boolean cursorIsFavorite,
            final int size
    ) {
        return festivalRepository.findFestivalsUsingCursor(
                userId,
                cursorTitle,
                cursorIsFavorite,
                getPageRequestWithSort(size, getFestivalSort())
        );
    }

    @Transactional(readOnly = true)
    public Optional<FestivalCursorDTO> findFestivalCursor(final long userId, final long festivalId) {
        return festivalRepository.findFestivalCursor(userId, festivalId);
    }

    @Transactional(readOnly = true)
    public List<Festival> getRecentFestivals(final int size) {
        return festivalRepository.findAllByFestivalEndAtGreaterThanEqual(
                LocalDateTime.now(),
                getPageRequest(size, getRecentFestivalsSort())
        );
    }

    private PageRequest getPageRequest(final int size, final Sort sort) {
        return PageRequest.of(INIT_PAGE, size, sort);
    }

    private Sort getRecentFestivalsSort() {
        return Sort.by(
                Order.asc(START_AT_COLUMN)
        );
    }
}
