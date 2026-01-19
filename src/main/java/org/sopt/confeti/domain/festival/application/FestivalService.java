package org.sopt.confeti.domain.festival.application;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.sopt.confeti.api.dummy.facade.dto.festival.request.CreateFestivalDateDTO;
import org.sopt.confeti.api.performance.facade.dto.response.FestivalDetailDTO;
import org.sopt.confeti.domain.festival.Festival;
import org.sopt.confeti.domain.festival.application.dto.FestivalCursorDTO;
import org.sopt.confeti.domain.festival.infra.repository.FestivalRepository;
import org.sopt.confeti.domain.festival_date.FestivalDate;
import org.sopt.confeti.domain.festival_date.application.FestivalDateService;
import org.sopt.confeti.domain.festival_stage.application.FestivalStageService;
import org.sopt.confeti.domain.festival_time.application.FestivalTimeService;
import org.sopt.confeti.global.common.redis.RedisHandler;
import org.sopt.confeti.global.common.redis.RedisKey;
import org.sopt.confeti.global.exception.NotFoundException;
import org.sopt.confeti.global.message.ErrorMessage;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class FestivalService {

    private static final String START_AT_COLUMN = "startAt";
    private static final int INIT_PAGE = 0;
    private static final String TITLE_COLUMN = "title";

    private final FestivalRepository festivalRepository;
    private final FestivalDateService festivalDateService;
    private final FestivalStageService festivalStageService;
    private final FestivalTimeService festivalTimeService;
    private final RedisHandler redisHandler;

    @Transactional(readOnly = true)
    public Festival findById(Long festivalId) {
        return festivalRepository.findById(festivalId)
            .orElseThrow(() -> new NotFoundException(ErrorMessage.NOT_FOUND));
    }

    // TODO: AOP 방식으로 캐싱 전략 수정
    @Transactional(readOnly = true)
    public FestivalDetailDTO getExpectedFestivalDetailByFestivalId(long festivalId) {
        Optional<FestivalDetailDTO> cachedFestival = redisHandler.get(
            RedisKey.PERFORMANCE_FESTIVALS.createKeyInfo(festivalId));
        if (cachedFestival.isPresent()) {
            return cachedFestival.get();
        }

        Festival festival = festivalRepository.findExpectedWithDatesById(festivalId)
            .orElseThrow(() -> new NotFoundException(ErrorMessage.NOT_FOUND));

        festivalDateService.loadDatesWithStagesByFestivalId(festivalId);
        festivalStageService.loadStagesWithTimesByFestivalId(festivalId);
        festivalTimeService.loadTimesWithArtistsByFestivalId(festivalId);
        festivalRepository.findExpectedWithReservationUrlsById(festivalId);

        FestivalDetailDTO festivalDetail = FestivalDetailDTO.from(festival);
        redisHandler.set(RedisKey.PERFORMANCE_FESTIVALS.createKeyInfo(festivalId), festivalDetail);
        return festivalDetail;
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
            Order.asc(TITLE_COLUMN)
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
    public Optional<FestivalCursorDTO> findFestivalCursor(final long userId,
        final long festivalId) {
        return festivalRepository.findFestivalCursor(userId, festivalId);
    }

    @Transactional(readOnly = true)
    public List<Festival> getRecentFestivals(final int size) {
        return festivalRepository.findAllByEndAtGreaterThanEqual(
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

    @Transactional
    public long create(Festival festival) {
        return festivalRepository.save(festival).getId();
    }

    @Transactional
    public void addDates(long festivalId, List<CreateFestivalDateDTO> dates) {
        Festival festival = findById(festivalId);

        festival.addDates(
            dates.stream()
                .map(FestivalDate::create)
                .toList()
        );
    }

    @Transactional(readOnly = true)
    public List<Festival> getAll() {
        return festivalRepository.findAll();
    }
}
